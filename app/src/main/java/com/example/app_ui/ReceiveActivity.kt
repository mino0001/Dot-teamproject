package com.example.app_ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivityReceiveBinding


class ReceiveActivity : ComponentActivity() {
    private  lateinit var adapter: NftSendAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /***전송 받은 nft list 바인딩***/
        val selectedItems = intent.getBooleanArrayExtra("selectedItems")?.toMutableList()
        val binding = ActivityReceiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filteredList: MutableList<Nft> = nftList.filterIndexed { index, _ -> selectedItems!![index] }.toMutableList<Nft>()
        adapter = NftSendAdapter(filteredList)



        //리사이클러 뷰
        binding.rvReceivedNftlist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvReceivedNftlist.adapter = adapter
        binding.rvReceivedNftlist.setHasFixedSize(true)

        /*** sender 정보 받아서 데이터 넣기 ***/
        binding.tvSenderInfoName
        binding.tvSenderInfoAddress
        binding.tvMemo


        /***수락 버튼 -> sender 로딩 화면 종료***/
        binding.btnAccept.setOnClickListener {

            /**
             * nft 받기
             * -> noti info 로 이동, sender, receiver 알림 추가
             */


        }

        /*** 거절 버튼 -> sender 로딩 화면 종료***/
        binding.btnDecline.setOnClickListener{
            /**
             * finish()
             * -> noti info로 이동 sender, receiver 알림 추가
             */

        }


    }
}
