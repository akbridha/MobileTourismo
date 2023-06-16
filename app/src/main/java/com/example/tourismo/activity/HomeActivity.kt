package com.example.tourismo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityGoBinding
import com.example.tourismo.viewmodel.HomeViewModel
import com.example.tourismo.viewmodel.LoginViewModel

class GoActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerView.Adapter<StringViewHolder>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var binding : ActivityGoBinding
    private lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            HomeViewModel::class.java)

//        viewModel.getAllTourist()
    val color = ContextCompat.getColor(this, R.color.dasar)
    binding.btnHome.setColorFilter(color)
    binding.recyclerView.setHasFixedSize(true)
    layoutManager = LinearLayoutManager(this)
    binding.recyclerView.layoutManager = layoutManager

    binding.btnSearch.setOnClickListener{pindahActivity("search")}
    binding.btnProfile.setOnClickListener {pindahActivity("profil")}
    binding.linearlayoutExplore.setOnClickListener{pindahActivity("tiket")}
    val dataList = getListOfItems() // Mendapatkan data daftar string

    adapter = StringAdapter(dataList)
    binding.recyclerView.adapter = adapter

}

    private fun pindahActivity(direction: String) {
        val animIn = when (direction) {
        "profil" -> R.anim.slide_in_right
        "search" -> R.anim.slide_in_right
        "tiket" -> R.anim.slide_in_up
        else -> R.anim.slide_in_left // Nilai default jika arah tidak valid
    }

        val animOut = when (direction) {
            "profil" -> R.anim.slide_out_left
            "search" -> R.anim.slide_out_left
            "tiket" -> R.anim.slide_out_down


            else -> R.anim.slide_out_right // Nilai default jika arah tidak valid
        }

        val intent = when (direction) {
            "profil" -> Intent(this, ProfilActivity::class.java)
            "search" -> Intent(this, ImgdetectActivity::class.java)
            "tiket" -> Intent(this, FindtickActivity::class.java)

            else -> Intent(this, GoActivity()::class.java) // Activity default jika arah tidak valid
        }

        startActivity(intent)
        finish()
        overridePendingTransition(animIn, animOut)
    }


    private inner class StringAdapter(private val dataList: List<ItemModel>) : RecyclerView.Adapter<StringViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            return StringViewHolder(view)
        }

        override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
            val item = dataList[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    private inner class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textview01)
        private val imageView :ImageView = itemView.findViewById(R.id.imageView)


        fun bind(stringItem: ItemModel) {
            textView1.text = stringItem.text
            imageView.setImageResource(stringItem.imageResId)

        }
    }

    private fun getListOfItems(): List<ItemModel> {
        val items = mutableListOf<ItemModel>()

        items.add(ItemModel("Touristive i", R.drawable.jam))
        items.add(ItemModel("Tourism", R.drawable.jam))
        items.add(ItemModel("It has", R.drawable.jam))
        items.add(ItemModel("and cu", R.drawable.jam))
        items.add(ItemModel("Tourism can es", R.drawable.jam))

        return items
    }


}

class ItemModel(val text: String, val imageResId: Int) {

}
