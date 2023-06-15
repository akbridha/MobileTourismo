package com.example.tourismo.activity

import Tiket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismo.api.response.TiketResponse

import com.example.tourismo.databinding.ActivityTiketBinding
import com.example.tourismo.databinding.TiketListBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONObject

class TiketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTiketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tiketResponse = intent.getStringExtra("tiketResponse")

        Log.d("TiketAct", tiketResponse.toString())

        // Inisialisasi RecyclerView dan adapter
        val adapter = tiketResponse?.let { parseTiketData(it) }?.let { TiketAdapter(it) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun parseTiketData(tiketResponse: String): List<Tiket> {
        // Parse tiketResponse ke objek menggunakan Gson
        val gson = Gson()
        val tiketResponseObj = gson.fromJson(tiketResponse, TiketResponse::class.java)

        // Buat variabel tiketList
        val tiketList = mutableListOf<Tiket>()

        // Ambil data bisnis dan tambahkan ke tiketList
        tiketResponseObj.maskapai?.bisnis?.let { bisnis ->
            bisnis.forEach { tiket ->
                tiketList.add(
                    Tiket(
                        "Bisnis",
                        tiket.airlines,
                        tiket.day,
                        tiket.departure,
                        tiket.arrival,
                        tiket.urlGambar
                    )
                )
            }
        }

        // Ambil data ekonomi dan tambahkan ke tiketList
        tiketResponseObj.maskapai?.ekonomi?.let { ekonomi ->
            ekonomi.forEach { tiket ->
                tiketList.add(
                    Tiket(
                        "Ekonomi",
                        tiket.airlines,
                        tiket.day,
                        tiket.departure,
                        tiket.arrival,
                        tiket.urlGambar
                    )
                )
            }
        }

        return tiketList
    }

    inner class TiketAdapter(private val data: List<Tiket>) : RecyclerView.Adapter<TiketAdapter.TiketViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiketViewHolder {
            val binding = TiketListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TiketViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TiketViewHolder, position: Int) {
            val tiket = data[position]
            holder.bind(tiket)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class TiketViewHolder(private val binding: TiketListBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(tiket: Tiket) {
                Picasso.get().load(tiket.urlGambar).into(binding.imageView)
                binding.textView1.text = tiket.airlines
                binding.textView2.text = "${tiket.days} days before \nyour schedule"
                binding.textView3.text = tiket.namaObjek
            }
        }
    }



}