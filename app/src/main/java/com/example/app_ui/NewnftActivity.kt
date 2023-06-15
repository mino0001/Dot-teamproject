package com.example.app_ui

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

import com.example.contract.MyNFT
import com.example.app_ui.databinding.ActivityNewnftBinding
import com.example.app_ui.databinding.ActivitySendBinding
import com.example.contract.MyNFT.NFTCREATED_EVENT
import com.google.firebase.database.DataSnapshot
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.generated.Uint256

import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthChainId
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.websocket.WebSocketService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.crypto.Credentials
import java.math.BigInteger
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


data class NFTData(
    val nft_hash: String = "",
    val nft_cg: String = "전체",
    val nft_alias: String = "(이름없음)"
)

var new_alias = "(이름없음)"

class NewnftActivity : ComponentActivity(){
    private lateinit var binding: ActivityNewnftBinding

    val web3j = Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/musyAUHHyrKtOkx90Ygr7A-q7_1AYfLH"))
    val contractAddress = "0x8481b9693fFabb79463B03566af2391ef150f957"
    lateinit var mynft: MyNFT

    private fun createNft(
        brand: String, modelName: String, manufacturer: String,
        purchaseSource: String, purchaseDate: BigInteger, additionalInfo: String
    ) {
        // Perform the necessary steps to create the NFT using the provided parameters
        // You can use the 'mynft' instance of your contract to interact with the smart contract functions.
        // Make sure you handle exceptions and callbacks appropriately.

        mynft.createNFT(
            brand,
            modelName,
            manufacturer,
            purchaseSource,
            purchaseDate,
            additionalInfo
        )
            .sendAsync()
            .thenApply { transactionReceipt: TransactionReceipt? ->
                runOnUiThread {
                    Toast.makeText(this@NewnftActivity, "트랜잭션이 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show()
                }
                if (transactionReceipt != null) {
                    newTokenId(transactionReceipt)
                }
                // TransactionReceipt 객체에서 토큰 아이디를 추출
                // val tokenId = extractTokenIdFromReceipt(transactionReceipt)
                // 토큰 아이디를 활용하여 추가 작업 수행
                // processTokenId(tokenId)
            }
            .exceptionally { throwable: Throwable? ->
                runOnUiThread {
                    Toast.makeText(this@NewnftActivity, "트랜잭션 전송 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
                null
            }
    }




    // 토큰 아이디 반환
    private fun newTokenId(transactionReceipt: TransactionReceipt) {
        val eventResponses = mynft.getNFTCreatedEvents(transactionReceipt)
        for (response in eventResponses) {

            // Firebase 실시간 데이터베이스에 tokenID 저장
            val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference: DatabaseReference = firebaseDatabase.reference
            val tokenId = response.tokenId
            val userNftRef: DatabaseReference = databaseReference.child("users").child(user_id).child("nft")

            userNftRef.get()
                .addOnSuccessListener { dataSnapshot: DataSnapshot ->
                    val nftCount = dataSnapshot.childrenCount.toInt()

                    val forNewNftRef: DatabaseReference = userNftRef.child(nftCount.toString())

                    println("Token ID: $tokenId") // DB 저장하는 로직으로 바꾸기

                    //val alias = findViewById<EditText>(R.id.et_new_alias).text.toString()

                    val nftData = NFTData(tokenId.toString(), "전체", new_alias)

                    println("생성된 NFT 저장중...")

                    forNewNftRef.setValue(nftData)
                        .addOnSuccessListener {
                            // 저장 성공적으로 완료됨
                            println("생성된 NFT 저장 완료")
                            // 원하는 작업 수행
                        }
                        .addOnFailureListener {
                            // 저장 실패
                            // 에러 처리 등 필요한 작업 수행
                        }


                }





        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewnftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 생성 버튼
        binding.btnNewMake.setOnClickListener {
            val brand = findViewById<EditText>(R.id.et_brand).text.toString()
            val modelName = findViewById<EditText>(R.id.et_model_name).text.toString()
            val manufacturer = findViewById<EditText>(R.id.et_manufacturer).text.toString()
            val purchaseSource = findViewById<EditText>(R.id.et_po).text.toString()
            val purchaseDate =
                BigInteger(findViewById<EditText>(R.id.et_date_purchase).text.toString())
            val additionalInfo = findViewById<EditText>(R.id.tv_new_memo).text.toString()
            val privateKey = findViewById<EditText>(R.id.et_private_key).text.toString()
            val alias = findViewById<EditText>(R.id.et_new_alias).text.toString()

            val credentials = Credentials.create(privateKey)

            new_alias = if (alias.trim().isNotEmpty()) alias.trim() else "(이름 없음)"
            mynft = MyNFT.load(contractAddress, web3j, credentials, DefaultGasProvider())
            createNft(brand, modelName, manufacturer, purchaseSource, purchaseDate, additionalInfo)
        }

        //취소 버튼
        binding.btnNewCancel.setOnClickListener {
            finish()
        }


//    private fun addNftItem() {
//        binding.btnNewMake.setOnClickListener {
//            //예시. 생성하기에서 입력받은 데이터 리사이클러 뷰에 추가
//            nftList.add(Nft(1,R.drawable.icon_nft2, "NFT1", "more_1", "카테고리1",false))
//        }
//    }
    }
}