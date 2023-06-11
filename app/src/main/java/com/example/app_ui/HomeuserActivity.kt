package com.example.app_ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.app_ui.databinding.ActivityHomeuserBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


private var binding: ActivityHomeuserBinding? = null

class HomeuserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeuserBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // 뒤로가기 버튼
        binding!!.btnBack.setOnClickListener{
            finish()
        }

        //클립보드 이미지 뷰
        binding!!.ivCopy.setOnClickListener{
            copyTextToClipboard(this,binding!!.tvUserWalletAddress.text.toString())
        }

        /***
         * 사용자 정보 QR 코드
         * 일단 homeuser_activity에 있는 예시 텍스트 지갑 주소로
         */
        var ImageQRcode = generaterQRCode(binding!!.tvUserWalletAddress.text.toString())
        binding!!.ivUserQr.setImageBitmap(ImageQRcode)
    }

    //QRCode 생성
    fun generaterQRCode(contents: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, 700, 700)
    }

    //클립보드
    fun copyTextToClipboard(context: Context, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
    }
}


