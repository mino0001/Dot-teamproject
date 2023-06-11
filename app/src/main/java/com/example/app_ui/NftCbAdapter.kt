package com.example.app_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_ui.databinding.ListNftItemCbBinding


//체크박스 있는 nft list adapter. send2_activity, edit_activity

class NftCbAdapter(private val nftList: MutableList<Nft>, private val selectAllListener: () -> Unit) : RecyclerView.Adapter<NftCbAdapter.ViewHolder>() {

    private val checkedItems = mutableListOf<Boolean>()  //check된 nft 넘김
    private var isSelectAll = false
    init {
        for (i in nftList.indices) {
            checkedItems.add(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNftItemCbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = nftList[position]
        val isChecked = checkedItems[position]

        holder.binding.cbNft.isChecked = isChecked
        holder.binding.ivNft.setImageResource(item.img_nft)
        holder.binding.tvNftAlias.text = item.alias
        holder.binding.tvNftMore.text = item.more

        holder.bind(nftList[position])

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 300
        holder.itemView.requestLayout()


        holder.itemView.setOnClickListener {
            val newCheckedState = !isChecked
            holder.binding.cbNft.isChecked = newCheckedState
            checkedItems[position] = newCheckedState
        }

    }

    override fun getItemCount(): Int {
        return nftList.size
    }

     fun selectAllItems() {
         isSelectAll = true
         checkedItems.fill(true)
         notifyDataSetChanged()
     }

     fun deselectAllItems() {
         isSelectAll = false
         checkedItems.fill(false)
         notifyDataSetChanged()
    }

    fun getSelectedItems() : MutableList<Boolean> {
        return checkedItems
    }

    inner class ViewHolder(val binding: ListNftItemCbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cbNft.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val newCheckedState = binding.cbNft.isChecked
                    checkedItems[position] = newCheckedState

                    if (isSelectAll && !newCheckedState) {
                        isSelectAll = false
                        selectAllListener()
                    }
                }
            }
        }
        fun bind(data: Nft) {
        }

    }
}


