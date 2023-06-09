package com.example.app_ui

import android.content.Intent
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

        binding!!.btnBack.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var ImageQRcode = generaterQRCode(binding!!.tvUserWalletAddress.text.toString())
        binding!!.ivUserQr.setImageBitmap(ImageQRcode) //일단 homeuser에 있는 예시 텍스트 지갑 주소로
    }

    //QRCode 생성
    private fun generaterQRCode(contents: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, 512, 512)
    }
}


