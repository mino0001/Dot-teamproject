package com.example.app_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_ui.databinding.ActivityNewloadingBinding

class NewLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewloadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityNewloadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

