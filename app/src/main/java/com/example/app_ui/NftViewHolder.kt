package com.example.app_ui

import android.content.Intent
import android.os.Parcelable
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

        if (data.alias!!.isEmpty()){
            binding.tvNftTitle.text = data.more
            binding.tvNftSubtitle.text = ""

        }else{
            binding.tvNftTitle.text = data.alias
            binding.tvNftSubtitle.text =data.more
        }


        itemView.setOnClickListener {
            val intent = Intent(context, NftinfoActivity::class.java)
            val position = adapterPosition
            intent.putExtra("data", data as Parcelable)
            intent.putExtra("position", position)
            context.startActivity(intent)

        }


   }




}