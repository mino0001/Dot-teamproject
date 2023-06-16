package com.example.app_ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivitySend2Binding
import com.example.contract.MyNFT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger


class Send2Activity : ComponentActivity() {
    private  lateinit var adapter: NftSendAdapter

    val web3j = Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/musyAUHHyrKtOkx90Ygr7A-q7_1AYfLH"))
    val contractAddress = "0x8481b9693fFabb79463B03566af2391ef150f957"
    lateinit var mynft: MyNFT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedItems = intent.getBooleanArrayExtra("selectedItems")?.toMutableList()
        val binding = ActivitySend2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val filteredList: MutableList<Nft> = nftList.filterIndexed { index, _ -> selectedItems!![index] }.toMutableList<Nft>()
        adapter = NftSendAdapter(filteredList)



        //리사이클러 뷰
        val dividerItemDecoration =
            DividerItemDecoration(binding!!.rvSendNftlist.context, LinearLayoutManager(this).orientation)
        binding!!.rvSendNftlist.addItemDecoration(dividerItemDecoration)
        binding.rvSendNftlist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvSendNftlist.adapter = adapter
        binding.rvSendNftlist.setHasFixedSize(true)

        //지갑주소 카메라 버튼
        binding.ivSendCamera.setOnClickListener{
            val integrator = IntentIntegrator(this);

            integrator.setOrientationLocked(false); //세로
            integrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다."); //QR코드 화면이 되면 밑에 표시되는 글씨 바꿀수있음
            qrScanResultLauncher.launch(integrator.createScanIntent())
        }

        //전송하기
        binding.btnTransmit.setOnClickListener {
            if(binding.etSendUserId.text.toString().trim().isNotEmpty()&&
                binding.etSendAddress.text.toString().trim().isNotEmpty()&&
                binding.etSendPw.text.toString().trim().isNotEmpty()) {

                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY // 이전 화면으로 못 돌아오도록

                val loadingIntent = Intent(this, LoadingActivity::class.java)
                startActivity(loadingIntent)
                Toast.makeText(this, "로딩중", Toast.LENGTH_SHORT).show()
                finish()

                /***
                요청 보내고, 핀 번호 일치 해야 nft 전송, 알림페이지에 알림 추가.

                1. 요청
                2-1. pin 번호 입력 -> 일치하면 nft 전송-> 알림창 추가
                2-2. 불일치 시 실패 알림 추가

                작업 완료
                loadingView.visibility = View.GONE
                val intent = Intent(this, NotiActivity::class.java)
                startActivity(intent)***/
                val userId = findViewById<EditText>(R.id.et_send_user_id).text.toString()
                val sendAddress = findViewById<EditText>(R.id.et_send_address).text.toString()
                val pin = findViewById<EditText>(R.id.et_send_pw).text.toString()
                val memo = findViewById<EditText>(R.id.tv_memo).text.toString()
                val privateKey = findViewById<EditText>(R.id.et_private_key).text.toString()
                var newAlias = "(이름 없음)"
                var nftCount = 0

                var credentials = Credentials.create(privateKey)
                mynft = MyNFT.load(contractAddress, web3j, credentials, DefaultGasProvider())

                //선택된 nft의 토큰id 배열에 넣는 로직 추가해야함
                /* for (배열 수만큼) {*/
                val tokenId = filteredList.map { it.more.toInt() }.toTypedArray()

                nftCount = 0
                for (i in tokenId) {
                    mynft.transferNFT(BigInteger.valueOf(i.toLong()), sendAddress)
                        .sendAsync()
                        .thenApply { transactionReceipt: TransactionReceipt? ->
                            // 트랜잭션이 성공적으로 전송된 후 실행될 코드
                            runOnUiThread {
                                Toast.makeText(
                                    this@Send2Activity,
                                    "트랜잭션이 성공적으로 전송되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                var firebaseDatabase = FirebaseDatabase.getInstance()
                                var reference = firebaseDatabase.getReference("users/${user_id}/nft")


                                println("print : " + i)

                                for ((index, nft) in filteredList.withIndex()) {
                                    if (nft.more.toInt() == i) {
                                        newAlias = nft.alias.toString()
                                        println("newAlias : " + newAlias)
                                        reference.child((nft.id-1).toString()).removeValue()
                                        filteredList.removeAt(index)
                                        adapter.notifyItemRemoved(index)
                                        break
                                    }
                                }


                                println("전송된 NFT 삭제완료")

                                var databaseReference: DatabaseReference = firebaseDatabase.reference

                                var userNftRef: DatabaseReference = databaseReference.child("users").child(userId).child("nft")

                                println("받은 NFT 저장중...")

                                userNftRef.get()
                                    .addOnSuccessListener { dataSnapshot: DataSnapshot ->

                                        if(nftCount==0){
                                            println("nftCount==0")
                                            nftCount = dataSnapshot.childrenCount.toInt()
                                        }
                                        else{
                                            println("nftCount!=0")
                                            nftCount++
                                        }

                                        var forNewNftRef: DatabaseReference = userNftRef.child(nftCount.toString())

                                        println("nftCount : " + nftCount)

                                        var nftData = NFTData(i.toString(), "전체", newAlias)

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




                            // 전송 완료 후 getTransferCount 함수 호출
                            // val tokenId = transactionReceipt?.tokenId[i]
                            // val transferCount = mynft.getTransferCount(tokenId).send()
                            val transferCount = mynft.getTransferCount(BigInteger.valueOf(i.toLong()))
                                .sendAsync()
                                .get()
                        }
                        .exceptionally { throwable: Throwable? ->
                            // 트랜잭션이 실패하거나 예외가 발생한 경우 실행될 코드
                            runOnUiThread {
                                Toast.makeText(
                                    this@Send2Activity,
                                    "트랜잭션 전송에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            null
                        }


                }



            }
                else{ //지갑 주소, 이름, PIN 번호 입력 안 했을 경우
                Toast.makeText(this, "정보를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
                }

            reset = 1
            //파이어베이스 갱신
            /*val firebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference: DatabaseReference = firebaseDatabase.reference
            val userNftRef: DatabaseReference = databaseReference.child("users").child(user_id).child("nft")

            userNftRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
                val nftList2 = mutableListOf<NFTData>()

                // 기존 nft 데이터를 불러와서 NFTData 배열에 저장
                for (childSnapshot in dataSnapshot.children) {
                    val nftData = childSnapshot.getValue(NFTData::class.java)
                    if (nftData != null) {
                        nftList2.add(nftData)
                    }
                }

                println("hello reset!")
                // 기존 nft 데이터 삭제
                userNftRef.removeValue()

                // 새로운 nft 데이터 추가
                for (index in 0 until nftList2.size) {
                    val newNftRef: DatabaseReference = userNftRef.child(index.toString())

                    println("hello for reset!")
                    val nftData = NFTData(
                        nftList2[index].nft_hash,
                        nftList2[index].nft_cg,
                        nftList2[index].nft_alias
                    )

                    newNftRef.setValue(nftData)
                        .addOnSuccessListener {
                            // 저장 성공적으로 완료됨
                            println("NFT 저장 완료")
                        }
                        .addOnFailureListener {
                            // 저장 실패
                            // 에러 처리 등 필요한 작업 수행
                        }
                }
            }*/

        }

        //취소 버튼 -뒤로가기
        binding.btnCancel.setOnClickListener{
            finish()

        }


    }

    private val qrScanResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val intent = result.data
        val integratorResult = IntentIntegrator.parseActivityResult(result.resultCode, intent)
        if (integratorResult != null) {
            if (integratorResult.contents == null) {
                Toast.makeText(this, "인식된 QR-data가 없습니다.", Toast.LENGTH_LONG).show()
            } else {
                val scannedText = integratorResult.contents
                // 스캔된 내용을 처리하는 로직
                copyTextToClipboard(scannedText)
                Toast.makeText(this, "텍스트가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()

                Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "QR스캔에 실패했습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun copyTextToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Label", text)
        clipboardManager.setPrimaryClip(clipData)
    }



}
