package com.example.app_ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_ui.databinding.FragmentMoreBinding

private lateinit var binding: FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val NOTIFICATION_ENABLED_KEY = "notification_enabled"
    companion object {
        fun newInstance() : MoreFragment {
            return MoreFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPref = requireActivity().getSharedPreferences("notification_pref", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        val notificationSwitch = binding.switchNoti // 알림 설정 스위치

        // 알림 설정 스위치 초기 상태 설정
        val notificationEnabled = sharedPref.getBoolean(NOTIFICATION_ENABLED_KEY, false)
        notificationSwitch.isChecked = notificationEnabled

        // 알림 설정 스위치 클릭 이벤트 처리
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            // 설정 상태 저장
            editor.putBoolean(NOTIFICATION_ENABLED_KEY, isChecked)
            editor.apply()

            if (isChecked) {
                // 알림 설정이 켜진 경우
                // 알림을 생성하고 등록하는 로직을 추가하세요.
            } else {
                // 알림 설정이 꺼진 경우
                // 알림을 취소하는 로직을 추가하세요.
            }
        }



        return view
    }



}