package com.example.app_ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.app_ui.databinding.ActivityHomeuserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        // 사용자 정보 QR 코드
        val database = FirebaseDatabase.getInstance().reference.child("users").child(user_id)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userWallet = snapshot.child("user_wallet").getValue(String::class.java)
                userWallet?.let {
                    binding!!.tvUserWalletAddress.text = it
                    val imageQRCode = generaterQRCode(it)
                    binding!!.ivUserQr.setImageBitmap(imageQRCode)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        //var ImageQRcode = generaterQRCode(binding!!.tvUserWalletAddress.text.toString())
        //binding!!.ivUserQr.setImageBitmap(ImageQRcode)
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


