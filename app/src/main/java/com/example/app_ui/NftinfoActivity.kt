package com.example.app_ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.app_ui.databinding.ActivityNftinfoBinding



class NftinfoActivity : ComponentActivity(){
    private var binding: ActivityNftinfoBinding? = null


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
        var data = intent.getParcelableExtra("data", Nft::class.java)
        binding!!.tvInfoTitle.text = data!!.alias


        //뒤로가기 버튼
        binding!!.btnBack.setOnClickListener{ finish() }

        //확인 버튼
        binding!!.btnSubmit.setOnClickListener{
            val selectedValue = binding!!.spinnerNftinfoCategory.selectedItem as String
            val position = intent.getIntExtra("position", -1)
            nftList[position].category = selectedValue

            finish()

        }

        /***
         * 일단 nft 주소로 qr 만들어 둠
         ***/
        var ImageQRcode = HomeuserActivity().generaterQRCode(binding!!.tvInfoAddress.text.toString())
        binding!!.ivNftQr.setImageBitmap(ImageQRcode)

    }


}