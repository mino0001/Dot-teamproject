package com.example.app_ui


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_ui.databinding.ActivityEditBinding
import com.example.app_ui.databinding.ActivityToolbarBinding


class EditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include.tvPage.setText("폴더 이동")
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = NftCbAdapter(nftList)
        binding.recyclerView.setHasFixedSize(true)

        binding.btnEditselect.setOnClickListener {

            var builder = AlertDialog.Builder(this)
            var listener = object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface, which: Int) {
                    //val selected_category = categoryArray[which]
                    dialog.dismiss()
                    intent = Intent(this@EditActivity, MainActivity::class.java)
                    startActivity(intent)

                }

            }
            builder.setTitle("카테고리 선택")
            builder.setItems(categoryArray,listener)
            builder.show()





        }

    }
}
