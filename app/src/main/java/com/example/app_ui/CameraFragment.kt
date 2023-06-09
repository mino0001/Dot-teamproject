package com.example.app_ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_ui.databinding.FragmentCameraBinding
import com.example.app_ui.databinding.FragmentHomeBinding
import com.google.zxing.integration.android.IntentIntegrator

class CameraFragment : Fragment() {

    private var fragmentCameraBinding : FragmentCameraBinding? =null
    lateinit var mainActivity: MainActivity
    companion object {
        const val TAG : String = "로그"

        fun newInstance() : CameraFragment {
            return CameraFragment()
        }

    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    // 뷰가 생성되었을 때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val binding : FragmentCameraBinding = FragmentCameraBinding.inflate(inflater,container,false)
        fragmentCameraBinding = binding

        //인증하기 버튼
        fragmentCameraBinding!!.btnQr1.setOnClickListener()
        {
            val integrator = IntentIntegrator(mainActivity);

            integrator.setOrientationLocked(false); //세로
            integrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다."); //QR코드 화면이 되면 밑에 표시되는 글씨 바꿀수있음
            integrator.initiateScan();
        }

        //불러오기
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
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(mainActivity, "인식된 QR-data가 없습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(mainActivity, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show()
                //QR코드 찍기 성공
                //여기에 작업하면 된다.
            }
        } else { //QR 코드 실패
            Toast.makeText(mainActivity, "QR스캔에 실패했습니다.", Toast.LENGTH_LONG).show()
        }
    }


}