package com.example.app_ui


import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.FragmentHomeBinding
import com.google.firebase.database.*


val nftList = mutableListOf<Nft>()
//var categoryArray = arrayOf("카테고리1", "카테고리2","카테고리3","카테고리4")
lateinit var categoryArray: Array<String>
var count = 0
lateinit var nftAdapter : NftAdapter

class HomeFragment : Fragment() {


    //뷰가 사라질 때, 즉 메모리에서 날라갈 때 같이 날리기 위해 따로 빼두기
    private var fragmentHomeBinding : FragmentHomeBinding? =null
    private var intent :Intent? = null
    lateinit var categoryArrayAll: Array<String>

    //private lateinit var nftList: MutableList<Nft>

    companion object {

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
                //사용자 정보
                R.id.btn_tb_user -> {
                    intent = Intent(context, HomeuserActivity::class.java)
                    startActivity(intent)

                    true
                }
                //알림
                R.id.btn_tb_noti -> {
                    intent = Intent(context, NotiActivity::class.java)
                    startActivity(intent)
                    true
                }
                //생성
                R.id.tb_nft_new -> {
                    intent = Intent(context, NewnftActivity::class.java)
                    startActivity(intent)
                    true
                }
                //전송
                R.id.tb_nft_send -> {
                    intent = Intent(context, SendActivity::class.java)
                    startActivity(intent)
                    true
                }
                //편집
                R.id.tb_category_edit -> {
                    intent = Intent(context, EditActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

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

        fragmentHomeBinding!!.rvNftList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        nftAdapter = NftAdapter(mutableListOf())
        fragmentHomeBinding!!.rvNftList.adapter = nftAdapter
        nftAdapter.notifyDataSetChanged()


    }

    private fun setupCategorySpinnerHandler(){

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        //val userId = "userid1"
        val categoryRef: DatabaseReference = databaseReference

        //TODO: 데이터베이스에서 카테고리 정보 가져오기
        categoryRef.child("users").child(user_id).child("category").child("cg_name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val categoryList: MutableList<String> = mutableListOf()

                    categoryList.add("전체")

                    for (snapshot in dataSnapshot.children) {
                        val cgName = snapshot.getValue(String::class.java)

                        cgName?.let {
                            categoryList.add(it)
                        }
                    }

                    categoryArray = categoryList.toTypedArray()
                    fragmentHomeBinding!!.spinnerCategory.adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        categoryArray
                    )


                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })

        // TODO: 데이터베이스에서 nft 정보 가져오기
        categoryRef.child("users").child(user_id).child("nft")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    nftList.clear()
                    count=0 //여기서 매번 count 초기화
                    for (snapshot in dataSnapshot.children) {


                        val nftHash = snapshot.child("nft_hash").getValue(String::class.java)
                        val nftCg = snapshot.child("nft_cg").getValue(String::class.java)
                        val nftAlias = snapshot.child("nft_alias").getValue(String::class.java)


                        if (nftHash != null && nftCg != null && nftAlias != null) {
                            count++
                            nftList.add(
                                Nft(
                                    count,
                                    R.drawable.icon_nft2,
                                    nftAlias,
                                    nftHash,
                                    nftCg,
                                    false
                                )
                            )
                        }
                    }


                    nftAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })


        /*fragmentHomeBinding!!.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        }*/



        fragmentHomeBinding!!.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(fragmentHomeBinding!!.spinnerCategory.getItemAtPosition(position).equals("전체")){
                        // 전체 카테고리 선택 시
                        nftAdapter = NftAdapter(nftList)
                    } else {
                        val selectedCategory = categoryArray[position]
                        val filteredList = nftList.filter { it.category == selectedCategory }
                        nftAdapter = NftAdapter(filteredList.toMutableList())
                    }
                    fragmentHomeBinding!!.rvNftList.adapter = nftAdapter
                    nftAdapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

}

