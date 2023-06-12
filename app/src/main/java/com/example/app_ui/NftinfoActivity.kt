package com.example.app_ui

import android.R
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.app_ui.databinding.ActivityNftinfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class NftinfoActivity : ComponentActivity(){
    private var binding: ActivityNftinfoBinding? = null

    // 파이어베이스 데이터베이스 레퍼런스 생성
    val database = FirebaseDatabase.getInstance().reference

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = ActivityNftinfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        /*** 고유 id, 주소, 시각, 카테고리, 별칭 등등 정보 바인딩해서
         *
         * 아래 변경하기. 지금은 별칭만 해둔 상태
         *
         * ***/
        var data = intent.getParcelableExtra("data", Nft::class.java)

        binding!!.tvInfoTitle.text = data!!.alias
        binding!!.tvInfoAddress.text = data!!.more

        binding!!.spinnerNftinfoCategory.adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            categoryArray
        )


        //뒤로가기 버튼
        binding!!.btnBack.setOnClickListener{ finish() }

        //확인 버튼 클릭 시 updateNftCategoryInFirebase 함수 호출
        binding!!.btnSubmit.setOnClickListener{
            val selectedValue = binding!!.spinnerNftinfoCategory.selectedItem as String
            val position = intent.getIntExtra("position", -1)
            nftList[position].category = selectedValue

            // 파이어베이스에 변경된 카테고리 정보 업데이트
            updateNftCategoryInFirebase(position, selectedValue)

            finish()

        }



        /***
         * 일단 nft 주소로 qr 만들어 둠
         ***/
        var ImageQRcode = HomeuserActivity().generaterQRCode(binding!!.tvInfoAddress.text.toString())
        binding!!.ivNftQr.setImageBitmap(ImageQRcode)

    }

    // 확인 버튼 클릭 시 호출되는 함수
    private fun updateNftCategoryInFirebase(position: Int, newCategory: String) {
        val nftHash = nftList[position].more

        val userId = user_id // 사용자 ID
        val userRef = database.child("users").child(userId)

        // 해당 사용자의 nft 리스트에 대한 레퍼런스 생성
        val nftRef = userRef.child("nft")


        val query: Query = nftRef.orderByChild("nft_hash").equalTo(nftHash)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nftHash = childSnapshot.child("nft_hash").getValue(String::class.java)

                    // 해당 데이터의 nft_cg 값을 변경
                    childSnapshot.ref.child("nft_cg").setValue(newCategory)

                    println("NFT Hash: $nftHash")
                    println("NFT Category Updated to: $newCategory")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }




}