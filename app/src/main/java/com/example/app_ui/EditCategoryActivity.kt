package com.example.app_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView

// 카테고리 추가 및 삭제하는 액티비티
class EditCategoryActivity : AppCompatActivity() {


    private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val itemList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        var arr : Array<String> = resources.getStringArray(R.array.category_list)
        itemList.addAll(arr)


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
            itemList.add(newItem)
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }
    }

    fun deleteItem(view: View) {
        val checkedItems = listView.checkedItemPositions
        for (i in itemList.size - 1 downTo 0) {
            if (checkedItems.get(i)) {
                itemList.removeAt(i)
            }
        }
        listView.clearChoices()
        adapter.notifyDataSetChanged()
    }

    fun moveItemUp(view: View) {
        val checkedItems = listView.checkedItemPositions
        for (i in 1 until itemList.size) {
            if (checkedItems.get(i) && !checkedItems.get(i - 1)) {
                val temp = itemList[i]
                itemList[i] = itemList[i - 1]
                itemList[i - 1] = temp
            }
        }
        listView.clearChoices()
        adapter.notifyDataSetChanged()
    }

    fun moveItemDown(view: View) {
        val checkedItems = listView.checkedItemPositions
        for (i in itemList.size - 2 downTo 0) {
            if (checkedItems.get(i) && !checkedItems.get(i + 1)) {
                val temp = itemList[i]
                itemList[i] = itemList[i + 1]
                itemList[i + 1] = temp
            }
        }
        listView.clearChoices()
        adapter.notifyDataSetChanged()
    }
}