package com.example.app_ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.app_ui.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import java.io.File


class MainActivity : AppCompatActivity() {

    private var activityMainBinding: ActivityMainBinding? =null
    private lateinit var homeFragment: HomeFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var moreFragment: MoreFragment
    var backButtonPressedOnce = false
    val channelId = "my_channel_id"
    val channelName = "My Channel"
    val importance = NotificationManager.IMPORTANCE_HIGH

    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.icon_logo_2)
        .setContentTitle("Notification Title")
        .setContentText("Notification Message")
        .setPriority(importance)

    // 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding =ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding = binding
        setContentView(activityMainBinding!!.root)



        //firebase 초기화 + 데이터베이스 연결
        FirebaseApp.initializeApp(this)

        // Firebase 데이터베이스 참조 가져오기
        val database = FirebaseDatabase.getInstance()

        // 데이터베이스 URL 설정
        val databaseUrl = "https://console.firebase.google.com/u/1/project/dot-project-by-bym" // 팀원이 제공한 데이터베이스 URL로 변경
        database.setPersistenceEnabled(true)
        database.setPersistenceCacheSizeBytes(52428800)

        // Firebase 데이터베이스에 연결
        val databaseRef = database.getReference("database-node")


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

        //로그인한 사용자의 아이디에 대한 address가 없으면 sms 인증


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
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() {
        val notificationId = 123 // 알림 ID
        val intent = Intent(this, MainActivity::class.java) // 알림을 클릭했을 때 실행할 인텐트
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("새로운 알림")
            .setContentText("새로운 데이터가 추가되었습니다.")
            .setSmallIcon(R.drawable.icon_logo_2)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }



}