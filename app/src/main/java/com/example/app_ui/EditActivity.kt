package com.example.app_ui


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivityEditBinding
import com.example.app_ui.databinding.ActivityToolbarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditActivity : ComponentActivity() {

    private  lateinit var adapter: NftCbAdapter
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseDatabase 초기화
        database = FirebaseDatabase.getInstance().reference


        adapter = NftCbAdapter(nftList) {
            if (binding.include.cbCategoryall.isChecked) {
                binding.include.cbCategoryall.isChecked = false
            }
        }


        binding.include.tvPage.setText("폴더 이동")

        //리사이클러뷰
        val dividerItemDecoration =
            DividerItemDecoration(binding!!.recyclerView.context, LinearLayoutManager(this).orientation)
        binding!!.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)


        //nft 목록 전체선택 체크박스
        binding.include.cbCategoryall.setOnCheckedChangeListener{ _, isChecked ->
            if (::adapter.isInitialized) {
                if (isChecked) {
                    adapter.selectAllItems()
                } else {
                    adapter.deselectAllItems()
                }
                adapter.notifyDataSetChanged()
            }

        }

        //폴더 이동 버튼
        binding.btnEditselect.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            val selectedIndices: List<Int> = selectedItems.mapIndexedNotNull { index, isSelected ->
                if (isSelected) index else null
            } //true인 인덱스 추출

            if(!selectedItems.contains(true)) {
                Toast.makeText(this, "이동할 NFT를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            var builder = AlertDialog.Builder(this)
            var listener = object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val selectedCategory = categoryArray[which] //선택된 카테고리

                    selectedIndices.forEach { index ->
                        if (index in 0 until nftList.size) {
                            // 인덱스가 nftList의 유효한 범위 내에 있는지 확인
                            // 해당 인덱스에 대한 데이터 변경
                            nftList[index].category = selectedCategory.toString()

                            // Firebase Realtime Database에 데이터 업데이트
                            val nftRef = database.child("users").child(user_id).child("nft").child(nftList[index].toString())
                            nftRef.child("nft_cg").setValue(selectedCategory.toString())

                        }
                    }
                    dialog.dismiss()

                    val intent = Intent(this@EditActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    finish()
                    startActivity(intent)
                }
            }

            builder.setTitle("카테고리 선택")

            /***
             * values 폴더 내 category_list.xml이 아닌 Homefragment에
             * '전체' 말고 똑같이 만들어 놓은 categoryArray 불러서 사용하고 있음
             * 수정 필요!
             * ***/
            builder.setItems(categoryArray,listener)
            builder.show()





        }

    }


}
