package com.example.app_ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.app_ui.databinding.ActivityEdit2Binding

class Edit2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEdit2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelected.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
