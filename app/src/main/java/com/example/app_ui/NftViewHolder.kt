package com.example.app_ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNftItemBinding


class NftViewHolder ( val binding: ListNftItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    init{
        binding.root.setOnClickListener(){

        }
    }

    fun bind(data: Nft) {

        binding.ivNft.setImageResource(data.img_nft)
        binding.tvNftAlias.text =data.alias
        binding.tvNftMore.text =data.more


        itemView.setOnClickListener {
            val intent = Intent(context, NftinfoActivity::class.java)
            intent.putExtra("data", data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            //intent.run { context.startActivity(this) }
        }

//        val pos = adapterPosition
//        if(pos!= RecyclerView.NO_POSITION)
//        {
//            itemView.setOnClickListener {
//                Intent(context, ProfileDetailActivity::class.java).apply {
//                    putExtra("data", item)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }.run { context.startActivity(this) }
//            }
//        }


        //binding.cbNft.isChecked =data.completed
   }




}