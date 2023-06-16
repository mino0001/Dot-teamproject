package com.example.app_ui

import android.R
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.app_ui.databinding.ActivityNftinfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.example.contract.MyNFT
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger


class NftinfoActivity : ComponentActivity() {
    private var binding: ActivityNftinfoBinding? = null

    // 데베 쓰면 지울부분
    val web3j =
        Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/musyAUHHyrKtOkx90Ygr7A-q7_1AYfLH"))
    val contractAddress = "0x8481b9693fFabb79463B03566af2391ef150f957"
    val privatekey = "c242d074e17fda39cf733a2523bd41f1ad6b15779adfb14dad7f36789eed7383"
    val credentials = Credentials.create(privatekey)
    val mynft = MyNFT.load(contractAddress, web3j, credentials, DefaultGasProvider())


    private fun getTokenData(tokenId: BigInteger) {
        Toast.makeText(this, "토큰 ID: $tokenId", Toast.LENGTH_SHORT).show()


        mynft.getTokenData(tokenId)
            .sendAsync()
            .thenAccept { tokenData ->
                println("토큰데이터 받음")
                /*
                    // 필드별로 변수에 저장
                val brand = tokenData?.brand
                val modelName = tokenData?.modelName
                val manufacturer = tokenData?.manufacturer
                val purchaseOrder = tokenData?.purchaseOrder
                val purchaseDate = tokenData?.purchaseDate
                val additionalInfo = tokenData?.additionalInfo
                val transferHistory = tokenData?.transferHistory

                println("$brand, $modelName, $manufacturer, $purchaseOrder, $purchaseDate, $additionalInfo, $transferHistory") */
            }
            .exceptionally { throwable: Throwable? ->
                // 트랜잭션이 실패하거나 예외가 발생한 경우 실행될 코드
                runOnUiThread {
                    println("트랜잭션 전송에 실패했습니다.")
                }
                null
            }

    }

    // 파이어베이스 데이터베이스 레퍼런스 생성
    val database = FirebaseDatabase.getInstance().reference

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = ActivityNftinfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        /*** 고유 id, 주소, 시각, 카테고리, 별칭 등등 정보 바인딩해서
         *
         * 아래 변경하기. 지금은 별칭만 해둔 상태
         *
         * ***/

        // TokenData 가져오기
        // 토큰아이디 받아오는거 구현해야함
        // null일 경우 0 반환 -> 오류메세지 띄워야함
        // val tokenId = BigInteger.valueOf(data?.tokenId ?: 0)

        //바인딩하기
        var data = intent.getParcelableExtra("data", Nft::class.java)

        if (data!!.alias!!.isEmpty()) {
            binding!!.tvInfoTitle.text = data!!.more //token id로
        } else {
            binding!!.tvInfoTitle.text = data!!.alias
        }


        if (data!!.add_info!!.equals("")) {
            binding!!.tvInfoAdditionalInfo.text = "없음"
        } else binding!!.tvInfoAdditionalInfo.text = data!!.add_info
        binding!!.tvInfoTitle.text = data!!.alias
        binding!!.tvInfoNftId.text = data!!.more

        binding!!.spinnerNftinfoCategory.adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            categoryArray
        )

        //MyNFT 컨트랙트 인스턴스 생성


        val tokenId = BigInteger.valueOf(28)
        if (tokenId == BigInteger.ZERO) {
            Toast.makeText(this, "Please enter a valid tokenId.", Toast.LENGTH_SHORT).show()
            return
        } else {
            Toast.makeText(this, "success.", Toast.LENGTH_SHORT).show()
            getTokenData(tokenId)
        }


        //뒤로가기 버튼
        binding!!.btnBack.setOnClickListener { finish() }

        //확인 버튼 클릭 시 updateNftCategoryInFirebase 함수 호출
        binding!!.btnInfoSubmit.setOnClickListener {
            binding!!.btnInfoSubmit.setOnClickListener {
                val selectedValue = binding!!.spinnerNftinfoCategory.selectedItem as String
                val position = intent.getIntExtra("position", -1)
                nftList[position].category = selectedValue

                // 파이어베이스에 변경된 카테고리 정보 업데이트
                updateNftCategoryInFirebase(position, selectedValue)

                finish()

            }


            /***
             * 일단 nft 주소로 qr 만들어 둠
             ***/
            var ImageQRcode =
                HomeuserActivity().generaterQRCode(binding!!.tvInfoNftId.text.toString())
            binding!!.ivNftQr.setImageBitmap(ImageQRcode)

        }
    }

    // 확인 버튼 클릭 시 호출되는 함수
    private fun updateNftCategoryInFirebase(position: Int, newCategory: String) {
        val nftHash = nftList[position].more

        val userId = user_id // 사용자 ID
        val userRef = database.child("users").child(userId)

        // 해당 사용자의 nft 리스트에 대한 레퍼런스 생성
        val nftRef = userRef.child("nft")


        val query: Query = nftRef.orderByChild("nft_hash").equalTo(nftHash)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nftHash = childSnapshot.child("nft_hash").getValue(String::class.java)

                    // 해당 데이터의 nft_cg 값을 변경
                    childSnapshot.ref.child("nft_cg").setValue(newCategory)

                    println("NFT Hash: $nftHash")
                    println("NFT Category Updated to: $newCategory")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }
}

