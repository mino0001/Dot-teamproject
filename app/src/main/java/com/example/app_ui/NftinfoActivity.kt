package com.example.app_ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.app_ui.databinding.ActivityNftinfoBinding
import java.io.Serializable


class NftinfoActivity : ComponentActivity(){
    private var binding: ActivityNftinfoBinding? = null
    //lateinit var datas : Nft
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = ActivityNftinfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        // 주소랑 시각 등등 정보 바인


        val data = intent.getParcelableExtra("data", Nft::class.java)

        binding!!.tvInfoTitle.text = data!!.alias //변경하기**, 주소, 시각, 보낸 사람, 해시 등
        binding!!.btnBack.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding!!.btnSubmit.setOnClickListener{
             binding!!.nftcategory.toString()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

//            카테고리 설정, 별명 -> 처리
        }

    }


}