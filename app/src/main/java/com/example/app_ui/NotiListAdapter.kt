package com.example.app_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNftItemBinding
import com.example.app_ui.databinding.ListNotiBinding

class NotiListAdapter (val notiList: MutableList<Noti>) : RecyclerView.Adapter<NotiListAdapter.ViewHolder> () {

    private lateinit var binding : ListNotiBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ListNotiBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = notiList.size

    override fun onBindViewHolder(holder: ViewHolder, positoin: Int){
        holder.bind(notiList[positoin])
    }

    inner class ViewHolder( val binding: ListNotiBinding) : RecyclerView.ViewHolder(binding.root){

        //private val txtTitle: TextView = itemView.findViewById(R.id.alarm_title)
        //private val txtSubTitle: TextView = itemView.findViewById(R.id.alarm_subtitle)

        fun bind(item: Noti){
            binding.alarmTitle.text = item.title
            binding.alarmSubtitle.text = item.subtitle

        }

    }
}