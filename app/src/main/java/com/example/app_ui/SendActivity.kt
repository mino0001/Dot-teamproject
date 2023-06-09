package com.example.app_ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivitySendBinding
import com.example.app_ui.databinding.ActivityToolbarBinding


class SendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.nftListItem.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.include.tvPage.setText("전송")
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = NftCbAdapter(nftList)
        binding.recyclerView.setHasFixedSize(true)


        binding.btnNftsend.setOnClickListener {
            val intent = Intent(this, Send2Activity::class.java)
            startActivity(intent)
        }

        binding.include.cbCategoryall.setOnClickListener{
            binding.recyclerView.adapter.apply {

            }
        }


    }
}
