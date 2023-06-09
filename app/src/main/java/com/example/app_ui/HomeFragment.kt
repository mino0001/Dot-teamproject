package com.example.app_ui


import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


val nftList = mutableListOf<Nft>()
var categoryArray = arrayOf("카테고리1", "카테고리2","카테고리3","카테고리4")
var count = 0
lateinit var nftAdapter : NftAdapter

class HomeFragment : Fragment() {


    //뷰가 사라질 때 즉 메모리에서 날라갈 때 같이 날리기 위해 따로 빼두기
    private var fragmentHomeBinding : FragmentHomeBinding? =null
    private var intent :Intent? = null
    companion object {
        const val TAG : String = "로그"

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeBinding!!.toolbar.inflateMenu(R.menu.toolbar_menu)
       initRecycler()
        setupCategorySpinnerHandler()


        fragmentHomeBinding!!.btnFolder.setOnClickListener {
            intent = Intent(context, EditCategoryActivity::class.java)
            startActivity(intent)
        }

        fragmentHomeBinding!!.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_tb_user -> {
                    intent = Intent(context, HomeuserActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.btn_tb_noti -> {
                    intent = Intent(context, NotiActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.tb_nft_new -> { //생성
                    intent = Intent(context, SendActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.tb_nft_send -> { //전송
                    intent = Intent(context, SendActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.tb_category_edit -> { //편집
                    intent = Intent(context, EditActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 뷰가 생성되었을 때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val binding : FragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        fragmentHomeBinding = binding

        return fragmentHomeBinding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onDestroyView() {
        fragmentHomeBinding = null
        super.onDestroyView()
    }



    fun initRecycler(){


        fragmentHomeBinding!!.rvNftList.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(fragmentHomeBinding!!.rvNftList.context, LinearLayoutManager(context).orientation)
        fragmentHomeBinding!!.rvNftList.addItemDecoration(dividerItemDecoration)
        //val decoration = DividerItemDecoration(fragmentHomeBinding!!.rvNftList.context, VERTICAL)
        //fragmentHomeBinding!!.rvNftList.addItemDecoration(decoration)
        fragmentHomeBinding!!.rvNftList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        nftAdapter  = NftAdapter(nftList)
        fragmentHomeBinding!!.rvNftList.adapter = nftAdapter
        nftAdapter.notifyDataSetChanged()


    }

    private fun setupCategorySpinnerHandler(){
        fragmentHomeBinding!!.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                if(count==0){
                    count++
                    nftList.apply {
                        add(Nft(1,R.drawable.icon_nft2, "NFT1", "more_1", "카테고리1",false))
                        add(Nft(2,R.drawable.icon_nft2, "nft_ex_2", "more_2", "카테고리2",false))
                        add(Nft(3,R.drawable.nft, "nft_ex_3", "more_3", "카테고리3",false))
                        add(Nft(4,R.drawable.nft, "nft_ex_4", "more_4", "카테고리1",false))
                        add(Nft(5,R.drawable.icon_nft2, "nft_ex_5", "more_5", "카테고리4",false))
                        add(Nft(6,R.drawable.nft, "nft_ex_6", "more_6", "카테고리1",false))
                        add(Nft(7,R.drawable.nft, "nft_ex_7", "more_7", "카테고리3",false))
                        add(Nft(8,R.drawable.nft, "nft_ex_8", "more_8", "카테고리2",false))
                    }
                }


                if(fragmentHomeBinding!!.spinnerCategory.getItemAtPosition(position).equals("전체")){
                    nftAdapter=NftAdapter(nftList)
                }
                else {
                    val filteredList = nftList.filter { it.category == fragmentHomeBinding!!.spinnerCategory.getItemAtPosition(position)}
                    nftAdapter =NftAdapter(filteredList.toMutableList())
                }

                fragmentHomeBinding!!.rvNftList.adapter = nftAdapter
                nftAdapter.notifyDataSetChanged()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

}

