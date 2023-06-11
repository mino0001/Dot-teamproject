package com.example.app_ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.app_ui.databinding.ActivityLoginBinding


class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /*** 로그인 성공 시 호출되는 함수
    private fun saveLoginToken(token: String) {
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }
    ***/
}

