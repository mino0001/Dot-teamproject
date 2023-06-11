package com.example.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNftItemBinding

//home화면 nft list 어댑터
class NftAdapter( val nftList: MutableList<Nft>) : RecyclerView.Adapter<NftViewHolder>() {

    private lateinit var binding : ListNftItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftViewHolder {
        binding = ListNftItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NftViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NftViewHolder, position: Int) {
        val item = nftList[position]
        holder.bind(item)

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 300
        holder.itemView.requestLayout()

    }

    override fun getItemCount(): Int {
        return nftList.size
    }


}