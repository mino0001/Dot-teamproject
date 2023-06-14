// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/utils/Counters.sol";

contract MyNFT is ERC721 {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;
    
    struct TokenData {
        string brand;
        string modelName;
        string manufacturer;
        string purchaseOrder;
        uint256 purchaseDate;
        string additionalInfo;
        string[] transferHistory;
    }
    
    mapping(uint256 => TokenData) private _tokenData;
    
    event NFTCreated(uint256 indexed tokenId, address indexed creator);
    event NFTTransferred(uint256 indexed tokenId, address indexed from, address indexed to);
    
    constructor() ERC721("MyNFT", "NFT") {}
    
    function createNFT(
        string memory brand,
        string memory modelName,
        string memory manufacturer,
        string memory purchaseOrder,
        uint256 purchaseDate,
        string memory additionalInfo
    ) external returns (uint256) {
        _tokenIds.increment();
        uint256 newTokenId = _tokenIds.current();
        
        _mint(msg.sender, newTokenId);
        
        TokenData storage token = _tokenData[newTokenId];
        token.brand = brand;
        token.modelName = modelName;
        token.manufacturer = manufacturer;
        token.purchaseOrder = purchaseOrder;
        token.purchaseDate = purchaseDate;
        token.additionalInfo = additionalInfo;
        token.transferHistory.push("Created");

        // additionalInfo에 입력된 값이 없는 경우 기본값 "nothing"
        if (bytes(additionalInfo).length > 0) {
            token.additionalInfo = additionalInfo;
        }    
        else {
            token.additionalInfo = "nothing";
        }
        
        emit NFTCreated(newTokenId, msg.sender);
        
        return newTokenId;
    }
    
    function transferNFT(uint256 tokenId, address recipient) external {
        require(_isApprovedOrOwner(msg.sender, tokenId), "You are not the owner or approved to transfer this token.");
        
        _transfer(msg.sender, recipient, tokenId);
        
        // 토큰의 전송 기록을 저장합니다.
        TokenData storage token = _tokenData[tokenId];
        token.transferHistory.push(string(abi.encodePacked("Transferred to: ", recipient)));
        
        emit NFTTransferred(tokenId, msg.sender, recipient);
    }
    
    function getTokenData(uint256 tokenId) external view returns (TokenData memory) {
        return _tokenData[tokenId];
    }

    // 전송 이력 조회
    function getTransferHistory(uint256 tokenId) external view returns (string[] memory) {
        return _tokenData[tokenId].transferHistory;
    }
    
    // 전송 횟수 반환
    function getTransferCount(uint256 tokenId) external view returns (uint256) {
        return _tokenData[tokenId].transferHistory.length - 1;
    }
}
