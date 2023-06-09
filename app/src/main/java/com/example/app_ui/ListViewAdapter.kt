package com.example.app_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_ui.databinding.ListCategoryBinding


class ListViewAdapter
    (val context : Context, val list : ArrayList<CategoryName>)
    : BaseAdapter()
 {

          private lateinit var binding : ListCategoryBinding

     override fun getCount(): Int {
         return list.size
     }

     override fun getItem(position: Int): Any {
         return list[position]
     }

     override fun getItemId(position: Int): Long {
         return 0
     }

     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
         val view : View = LayoutInflater.from(context).inflate(R.layout.list_category, null)
         binding = ListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         binding.tvSelectcategory.text = list[position].name

         return view
     }


 }