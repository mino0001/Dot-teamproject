package com.example.app_ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_ui.databinding.ActivityEditBinding
import com.example.app_ui.databinding.ActivitySend2Binding
import com.example.app_ui.ui.theme.App_uiTheme

class Send2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySend2Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnTransmit.setOnClickListener {
//            val intent = Intent(this, NotiActivity::class.java)
//            startActivity(intent)
//
//        }
    }
}
