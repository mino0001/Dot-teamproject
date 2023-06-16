package com.example.app_ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_ui.databinding.FragmentCameraBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator

class CameraFragment : Fragment() {
    private var fragmentCameraBinding : FragmentCameraBinding? =null
    lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance() : CameraFragment {
            return CameraFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val binding : FragmentCameraBinding = FragmentCameraBinding.inflate(inflater,container,false)
        fragmentCameraBinding = binding

        /***
         *
         * 인증하기 + 불러오기
         *
         */

        // 인증하기 버튼
        fragmentCameraBinding!!.btnQr1.setOnClickListener()
        {
            val integrator = IntentIntegrator(mainActivity);

            integrator.setOrientationLocked(false); //세로
            integrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다."); //QR코드 화면이 되면 밑에 표시되는 글씨 바꿀수있음
            integrator.initiateScan();
        }

        // 불러오기 버튼
        fragmentCameraBinding!!.btnQr2.setOnClickListener()
        {
            val integrator = IntentIntegrator(mainActivity);

            integrator.setOrientationLocked(false); //세로
            integrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다."); //QR코드 화면이 되면 밑에 표시되는 글씨 바꿀수있음
            integrator.initiateScan();
        }


        return fragmentCameraBinding!!.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(mainActivity, "인식된 QR-data가 없습니다.", Toast.LENGTH_LONG).show()
            } else {

                val qrCodeValue = result.contents

                // 파이어베이스에서 users의 userid 값과 비교하여 메시지 표시
                val usersRef = FirebaseDatabase.getInstance().getReference("users")
                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var isAuthenticated = false

                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key

                            if (userId == qrCodeValue) {
                                isAuthenticated = true
                                break
                            }
                        }

                        val message = if (isAuthenticated) {
                            "인증된 사용자입니다."
                        } else {
                            "인증되지 않은 사용자입니다."
                        }

                        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // 에러 처리 등 필요한 작업 수행
                    }
                })


            }
        } else { //QR 코드 실패
            Toast.makeText(mainActivity, "QR스캔에 실패했습니다.", Toast.LENGTH_LONG).show()
        }
    }


}