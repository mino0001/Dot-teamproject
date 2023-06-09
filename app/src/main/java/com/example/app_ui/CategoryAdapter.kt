package com.example.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListCategoryBinding

class CategoryAdapter (private val categoryArray: ArrayList<CategoryName>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    private lateinit var binding : ListCategoryBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        binding = ListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryArray[position])
    }

    override fun getItemCount(): Int {
        return categoryArray.size
    }

    class CategoryViewHolder (val binding: ListCategoryBinding)
        :RecyclerView.ViewHolder(binding.root){

            fun bind(data: CategoryName){
                binding.tvSelectcategory.text = data.name
            }

    }
}