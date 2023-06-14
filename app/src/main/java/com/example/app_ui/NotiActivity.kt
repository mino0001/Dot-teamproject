package com.example.app_ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivityNotiBinding

class NotiActivity : AppCompatActivity() {



    lateinit var AlarmPageListAdapter: NotiListAdapter
    lateinit var binding: ActivityNotiBinding
    private val noti = mutableListOf<Noti>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmRecycler()


        binding.btnBack.setOnClickListener{

            //finish() 처리할지 고민
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun alarmRecycler(){
        AlarmPageListAdapter = NotiListAdapter(noti)
        binding.alarmRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.alarmRecyclerView.adapter = AlarmPageListAdapter

        /**
         * 알림 데이터 임시로 넣어둠
         */
        noti.apply{
            add(Noti(title = "수신 알림", subtitle = "NFT2 수신 완료"))
            add(Noti(title = "발신 알림", subtitle = "NFT5 발신 완료"))
            add(Noti(title = "수신 알림", subtitle = "NFT1 수신 완료"))
        }

        AlarmPageListAdapter.notifyDataSetChanged()
    }
}
