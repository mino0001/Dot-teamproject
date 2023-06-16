package com.example.app_ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.ListView
import com.example.app_ui.databinding.ActivityEditCategoryBinding

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
//import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


// 카테고리 추가 및 삭제하는 액티비티
class EditCategoryActivity : AppCompatActivity() {


    //private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val itemList = ArrayList<String>()
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityEditCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var arr : Array<String> = resources.getStringArray(R.array.category_list)

        itemList.addAll(categoryArray)
        itemList.removeAt(0)

        // FirebaseDatabase 초기화
        database = FirebaseDatabase.getInstance().reference

        listView = findViewById(R.id.lv_edit_category)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, itemList)
        listView.adapter = adapter

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            listView.setItemChecked(position, true)
            true
        }

        binding.btnAdd.setOnClickListener{
            addItem()
        }

        binding.btnDelete.setOnClickListener{
            deleteItem()
        }

        binding.btnEditCateSubmit.setOnClickListener{
            saveChangesToFirebase()
        }
    }


    fun addItem() {
        //var binding = ActivityEditCategoryBinding.inflate(layoutInflater)

        val editText = binding.etAddNewCate.text
        val newItem = editText.toString().trim()
        if (newItem.isNotEmpty()) {
            // categoryArray에 새로운 카테고리 추가
            //categoryArray = categoryArray.plus(newItem)
            itemList.add(newItem)
            adapter.notifyDataSetChanged()
            editText.clear()

        }
    }

    fun deleteItem() {
        val checkedItems = listView.checkedItemPositions
        val deletedItems = mutableListOf<String>()

        for (i in itemList.size - 1 downTo 0) {
            if (checkedItems.get(i)) {
                val deletedItem = itemList[i]
                itemList.removeAt(i)
                deletedItems.add(deletedItem)
            }
        }
        //categoryArray = categoryArray.filterNot { it in deletedItems.toTypedArray() }.toTypedArray()
        listView.clearChoices()
        adapter.notifyDataSetChanged()


    }

    fun saveChangesToFirebase() {
        // 수정된 카테고리 정보를 Firebase에 업데이트하는 로직 작성

        // 예시: Firebase의 "categories" 컬렉션을 업데이트
        //val updatedCategories: ArrayList<String> = itemList
        val database = FirebaseDatabase.getInstance()
        val categoryRef = database.reference.child("users").child(user_id).child("category").child("cg_name")


// 업데이트 수행
        categoryRef.setValue(itemList)
            .addOnSuccessListener {
                // 업데이트 성공
                // 처리할 작업 추가

                // MainFragment로 데이터 전달
                val homeFragment = HomeFragment()
                val bundle = Bundle()
                bundle.putStringArrayList("categoryList", itemList)
                homeFragment.arguments = bundle

                // homeragment로 화면 전환
//               supportFragmentManager.beginTransaction()
//                    .replace(R.id.container_home, homeFragment)
//                    .commit()

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                // 업데이트 실패
                // 에러 처리 로직 추가
            }
    }


}