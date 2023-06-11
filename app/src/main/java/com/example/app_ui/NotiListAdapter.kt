package com.example.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNotiBinding

class NotiListAdapter (val notiList: MutableList<Noti>) : RecyclerView.Adapter<NotiListAdapter.ViewHolder> () {

    //private lateinit var binding : ListNotiBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ListNotiBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = notiList.size

    override fun onBindViewHolder(holder: ViewHolder, positoin: Int){
        holder.bind(notiList[positoin])
    }

    inner class ViewHolder( val binding: ListNotiBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Noti){
            binding.alarmTitle.text = item.title
            binding.alarmSubtitle.text = item.subtitle

        }

    }
}