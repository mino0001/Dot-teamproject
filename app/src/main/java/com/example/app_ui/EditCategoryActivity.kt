package com.example.app_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

// 카테고리 추가 및 삭제하는 액티비티
class EditCategoryActivity : AppCompatActivity() {


    private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val itemList = ArrayList<String>()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        var arr : Array<String> = resources.getStringArray(R.array.category_list)

        itemList.addAll(categoryArray)

        // FirebaseDatabase 초기화
        database = FirebaseDatabase.getInstance().reference

        listView = findViewById(R.id.lv_edit_category)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, itemList)
        listView.adapter = adapter

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            listView.setItemChecked(position, true)
            true
        }
    }


    fun addItem(view: View) {
        editText = findViewById(R.id.editText)
        val newItem = editText.text.toString().trim()
        if (newItem.isNotEmpty()) {
            // categoryArray에 새로운 카테고리 추가
            categoryArray = categoryArray.plus(newItem)
            itemList.add(newItem)
            adapter.notifyDataSetChanged()
            editText.text.clear()

            // Firebase Realtime Database에 데이터 추가
            val categoryRef = database.child("users").child(user_id).child("category").child("cg_name")
            categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryData = snapshot.getValue() as? ArrayList<String>
                    if (categoryData != null) {
                        // 새로운 카테고리 추가
                        categoryData.add(newItem)
                        // 업데이트된 데이터 다시 설정
                        categoryRef.setValue(categoryData)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 데이터 가져오기 실패 시 처리
                }
            })


        }
    }

    fun deleteItem(view: View) {
        val checkedItems = listView.checkedItemPositions
        val deletedItems = mutableListOf<String>()

        for (i in itemList.size - 1 downTo 0) {
            if (checkedItems.get(i)) {
                val deletedItem = itemList[i]
                itemList.removeAt(i)
                deletedItems.add(deletedItem)
            }
        }
        categoryArray = categoryArray.filterNot { it in deletedItems.toTypedArray() }.toTypedArray()
        listView.clearChoices()
        adapter.notifyDataSetChanged()

        // Firebase Realtime Database에서 데이터 삭제
        val categoryRef = database.child("users").child(user_id).child("category").child("cg_name")
        categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryData = snapshot.getValue() as? ArrayList<String>
                if (categoryData != null) {
                    // 삭제된 카테고리 제거
                    categoryData.removeAll(deletedItems)
                    // 업데이트된 데이터 다시 설정
                    categoryRef.setValue(categoryData)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 가져오기 실패 시 처리
            }
        })


    }
}