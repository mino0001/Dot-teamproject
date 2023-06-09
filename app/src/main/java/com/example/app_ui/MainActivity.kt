package com.example.app_ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.app_ui.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var activityMainBinding: ActivityMainBinding? =null
    private lateinit var homeFragment: HomeFragment
    private lateinit var cameraFragment: CameraFragment

    private lateinit var moreFragment: MoreFragment

    companion object {

        const val TAG: String = "로그"

    }

    // 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding =ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding = binding
        // 레이아웃과 연결
        setContentView(activityMainBinding!!.root)

        Log.d(TAG, "MainActivity - onCreate() called")


        binding.btmNav.setOnItemSelectedListener(onBottomNavItemSelectedListener)
        binding.btmNav.setItemIconTintList(null);
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, homeFragment).commit()
        //처음에만 add, 나머지는 replace
    }

//    override fun onRestart() {
//        super.onRestart()
//        HomeFragment().initRecycler()
//    }


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
                Log.d(TAG, "MainActivity - 프로필버튼 클릭!")
                moreFragment = MoreFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, moreFragment).commit()
            }
        }

        true
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        Log.d(TAG, "MainActivity - onNavigationItemSelected() called ")
//
//        when(item.itemId){
//            R.id.menu_home -> {
//                Log.d(TAG, "MainActivity - 홈버튼 클릭!")
//            }
//            R.id.menu_ranking -> {
//                Log.d(TAG, "MainActivity - 랭킹버튼 클릭!")
//            }
//            R.id.menu_profile -> {
//                Log.d(TAG, "MainActivity - 프로필버튼 클릭!")
//            }
//        }
//
//        return true
//    }


}