package com.example.app_ui

import android.os.Bundle
import androidx.activity.ComponentActivity

import com.example.app_ui.databinding.ActivityNewnftBinding
import com.example.app_ui.databinding.ActivitySendBinding

class NewnftActivity : ComponentActivity(){
    private lateinit var binding: ActivityNewnftBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewnftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 생성 버튼
        binding.btnNewMake.setOnClickListener{

            /***
             *
             * nft 생성
             *
             ***/
        }

        //취소 버튼
        binding.btnNewCancel.setOnClickListener{
            finish()
        }


    }

//    private fun addNftItem() {
//        binding.btnNewMake.setOnClickListener {
//            //예시. 생성하기에서 입력받은 데이터 리사이클러 뷰에 추가
//            nftList.add(Nft(1,R.drawable.icon_nft2, "NFT1", "more_1", "카테고리1",false))
//        }
//    }

}