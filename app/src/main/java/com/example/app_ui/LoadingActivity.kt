package com.example.app_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.app_ui.databinding.ActivityLoadingBinding

// 전송하기 -> 로딩 화면
class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}