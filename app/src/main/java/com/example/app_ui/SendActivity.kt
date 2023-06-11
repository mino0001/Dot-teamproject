package com.example.app_ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivitySendBinding



class SendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendBinding
    private  lateinit var adapter: NftCbAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NftCbAdapter(nftList) {
            if (binding.include.cbCategoryall.isChecked) {
                binding.include.cbCategoryall.isChecked = false
            }
        }

        binding.include.tvPage.setText("전송")
        val dividerItemDecoration =
            DividerItemDecoration(binding!!.recyclerView.context, LinearLayoutManager(this).orientation)
        binding!!.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)


        binding.btnNftsend.setOnClickListener {

            val selectedItems = adapter.getSelectedItems()
            val intent = Intent(this, Send2Activity::class.java)

            if(!selectedItems.contains(true)) {
                Toast.makeText(this, "전송할 NFT를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            intent.putExtra("selectedItems", selectedItems.toBooleanArray())
            startActivity(intent)
        }

        //nft 목록 전체선택 체크박스
        binding.include.cbCategoryall.setOnCheckedChangeListener{ _, isChecked ->
            if (::adapter.isInitialized) {
                if (isChecked) {
                    adapter.selectAllItems()
                } else {
                    adapter.deselectAllItems()
                }
                adapter.notifyDataSetChanged()
            }
        }




    }
}
