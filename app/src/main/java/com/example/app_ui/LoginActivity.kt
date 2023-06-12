package com.example.app_ui


import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.app_ui.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

var user_id="userid1"


class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth // FirebaseAuth 인스턴스
    //private val RC_SIGN_IN = 9001 // Google 로그인 요청 코드
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("로그", ""+result.resultCode)
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("로그", "진입2")
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google 로그인이 성공한 경우, Firebase에 인증 정보 전달
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken)

                } catch (e: ApiException) {
                    // Google 로그인 실패 처리
                    handleLoginFailure(e)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        // GoogleSignInOptions를 구성합니다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // GoogleSignInClient를 초기화합니다.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnLogin.setOnClickListener{

            /*val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()*/

            signInWithGoogle()



        }

    }

    /*private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 시 MainActivity로 이동
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // 로그인 실패 시 처리
                    val exception = task.exception
                    handleLoginFailure(exception)
                }
            }
    }*/


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent


        signInLauncher.launch(signInIntent)

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google 로그인이 성공한 경우, Firebase에 인증 정보 전달
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google 로그인 실패 처리
                handleLoginFailure(e)
            }
        }
    }*/

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 시 MainActivity로 이동
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // 로그인 실패 시 처리
                    val exception = task.exception
                    handleLoginFailure(exception)
                }
            }
    }



    private fun handleLoginFailure(error: Throwable?) {
        // 로그인 실패 시 호출되는 함수
        // 에러 처리를 수행할 수 있습니다.
        error?.let {
            // 예시: showErrorDialog(it.message)
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

