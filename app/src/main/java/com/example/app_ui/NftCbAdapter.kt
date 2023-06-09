package com.example.app_ui

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ActivitySendBinding
import com.example.app_ui.databinding.ListNftItemBinding
import com.example.app_ui.databinding.ListNftItemCbBinding
import com.example.app_ui.databinding.ListNotiBinding

class NftCbAdapter(val nftList: MutableList<Nft>) : RecyclerView.Adapter<NftCbAdapter.ViewHolder>() {

    private lateinit var binding: ListNftItemCbBinding
    private val checkboxStatus = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListNftItemCbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nftList[position])

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 300
        holder.itemView.requestLayout()
    }

    override fun getItemCount(): Int {
        return nftList.size
    }

    inner class ViewHolder(val binding: ListNftItemCbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var sendBinding: ActivitySendBinding

        fun bind(data: Nft) {
            binding.ivNft.setImageResource(data.img_nft)
            binding.tvNftAlias.text = data.alias
            binding.tvNftMore.text = data.more
            binding.cbNft.isChecked = data.is_checked

            binding.lyNft.setOnClickListener { //layout 클릭 시 체크
                binding.cbNft.isChecked = !(binding.cbNft.isChecked)

                /*if (sendBinding.include.cbCategoryall.isChecked) { //전체 선택 상태에서 클릭
                    sendBinding.include.cbCategoryall.isChecked = false
                }*/

            }

//            sendBinding.include.cbCategoryall.setOnClickListener {
//                binding.cbNft.isChecked = sendBinding.include.cbCategoryall.isChecked
//
//            }

        }

    }
}


