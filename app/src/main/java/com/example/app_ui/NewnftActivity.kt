package com.example.app_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.app_ui.databinding.ActivityEditBinding
import com.example.app_ui.databinding.ActivityNewnftBinding

class NewnftActivity : ComponentActivity(){

    val binding = ActivityNewnftBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)


    }

    private fun addNftItem() {
        binding.btnNewSubmit.setOnClickListener {
            //예시. 생성하기에서 입력받은 데이터 리사이클러 뷰에 추가
            nftList.add(Nft(1,R.drawable.icon_nft2, "NFT1", "more_1", "카테고리1",false))
        }
    }

}