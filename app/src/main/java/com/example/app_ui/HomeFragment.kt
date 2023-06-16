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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


val nftList = mutableListOf<Nft>()
lateinit var categoryArray: Array<String>
var count = 0
lateinit var nftAdapter : NftAdapter
var reset = 0


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

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

        if(reset == 1) {
            resetNftList()
        }

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

    private fun resetNftList(){
        //파이어베이스 갱신
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = firebaseDatabase.reference
        val userNftRef: DatabaseReference = databaseReference.child("users").child(user_id).child("nft")

        userNftRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            val nftList2 = mutableListOf<NFTData>()

            // 기존 nft 데이터를 불러와서 NFTData 배열에 저장
            for (childSnapshot in dataSnapshot.children) {
                val nftData = childSnapshot.getValue(NFTData::class.java)
                if (nftData != null) {
                    nftList2.add(nftData)
                }
            }

            println("hello reset!")
            // 기존 nft 데이터 삭제
            userNftRef.removeValue()

            // 새로운 nft 데이터 추가
            for (index in 0 until nftList2.size) {
                val newNftRef: DatabaseReference = userNftRef.child(index.toString())

                println("hello for reset!")
                val nftData = NFTData(
                    nftList2[index].nft_hash,
                    nftList2[index].nft_cg,
                    nftList2[index].nft_alias
                )

                newNftRef.setValue(nftData)
                    .addOnSuccessListener {
                        // 저장 성공적으로 완료됨
                        println("NFT 저장 완료")
                    }
                    .addOnFailureListener {
                        // 저장 실패
                        // 에러 처리 등 필요한 작업 수행
                    }
            }
        }

        reset = 0
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
                                    false,
                                    ""
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

