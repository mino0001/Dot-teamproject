package com.example.app_ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivitySend2Binding
import com.google.zxing.integration.android.IntentIntegrator


class Send2Activity : ComponentActivity() {
    private  lateinit var adapter: NftSendAdapter
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
            if(binding.etSendName.text.toString().trim().isNotEmpty()&&
                binding.etSendAddress.text.toString().trim().isNotEmpty()&&
                binding.etSendPw.text.toString().trim().isNotEmpty()){

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

            }else{ //지갑 주소, 이름, PIN 번호 입력 안 했을 경우
                Toast.makeText(this, "정보를 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }

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
