package com.example.app_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.app_ui.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView



class MainActivity : AppCompatActivity() {

    private var activityMainBinding: ActivityMainBinding? =null
    private lateinit var homeFragment: HomeFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var moreFragment: MoreFragment
    private var doubleBackToExitPressedOnce = false
    var backButtonPressedOnce = false


    // 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding =ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding = binding
        setContentView(activityMainBinding!!.root)

        /*** 로그인 상태 확인
         *
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            // 저장된 토큰이 있으면 자동으로 로그인
            // TODO: 로그인 처리 로직 추가
        } else {
            // 저장된 토큰이 없으면 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        ***/

        val onBackPressedDispatcher = this.onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, callback)

        binding.btmNav.setOnItemSelectedListener(onBottomNavItemSelectedListener)
        binding.btmNav.setItemIconTintList(null);
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, homeFragment).commit()
        //처음에만 add, 나머지는 replace
    }


    // 바텀네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSelectedListener = NavigationBarView.OnItemSelectedListener {

        when(it.itemId){
            R.id.menu_home -> {
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, homeFragment).commit()
            }
            R.id.menu_camera -> {
                cameraFragment = CameraFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, cameraFragment).commit()
            }
            R.id.menu_more -> {
                moreFragment = MoreFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, moreFragment).commit()
            }
        }

        true
    }

    // 뒤로가기 버튼 2번 누르면 종료
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val handler = Handler(Looper.getMainLooper())
            if (backButtonPressedOnce) {
                finish() // 앱 종료
            } else {
                backButtonPressedOnce = true
                Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

                // 2초 내에 뒤로 가기 버튼을 한 번 더 누르지 않으면 초기화
                handler.postDelayed({ backButtonPressedOnce = false }, 2000)
            }}
    }

}