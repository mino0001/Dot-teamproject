package com.example.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNftItemBinding



//nftAdapter랑 같은데 클릭 이벤트x (nftinfo_activity로 이동하지x)
class NftSendAdapter(private val nftList: MutableList<Nft>) : RecyclerView.Adapter<NftSendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = nftList[position]

        holder.binding.ivNft.setImageResource(item.img_nft)
        holder.binding.tvNftAlias.text = item.alias
        holder.binding.tvNftMore.text = item.more

        holder.bind(nftList[position])

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 300
        holder.itemView.requestLayout()

    }

    override fun getItemCount(): Int {
        return nftList.size
    }


    inner class ViewHolder(val binding: ListNftItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Nft) {
            binding.ivNft.setImageResource(data.img_nft)
            binding.tvNftAlias.text = data.alias
            binding.tvNftMore.text = data.more

        }
    }
}


