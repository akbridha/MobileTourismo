package com.example.tourismo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityProfilBinding

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityProfilBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        
        //sharedPreference

        val savedEmail = getSavedEmailFromSharedPreferences()
        if (savedEmail != null) {
            // Lakukan sesuatu dengan nilai savedEmail
            // Contoh: tampilkan di TextView
            binding.tvEmail.text = savedEmail
        } else {
            // Tidak ada nilai yang tersimpan di Shared Preferences
        }
        
        
        binding.btnHome.setOnClickListener { pindahActivity("home") }
        binding.btnSearch.setOnClickListener { pindahActivity("search") }

        val color = ContextCompat.getColor(this, R.color.dasar)
        binding.btnProfile.setColorFilter(color)






        val listView = binding.listView

        val items = listOf("Change Password", "About Us", "Log Out")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = items[position]
            // Lakukan tindakan sesuai dengan item yang dipilih
            when (selectedItem) {
                "Change Password" -> {
                    Toast.makeText(this,"Menuju opsi Change password", Toast.LENGTH_SHORT).show()
                }
                "About Us" -> {
                    Toast.makeText(this,"Menuju laman About Us", Toast.LENGTH_SHORT).show()
                }
                "Log Out" -> {

                    clearSharedPreferences()
                    // Lakukan tindakan yang sesuai setelah logout, misalnya pindah ke halaman login
                    Toast.makeText(this,"Proses Log out", Toast.LENGTH_SHORT).show()
                    pindahActivity("login")
                }
            }
        }






    }



    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun getSavedEmailFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }

    private fun pindahActivity(direction: String) {
        val animIn = when (direction) {
            "search" -> R.anim.slide_in_left
            "home" -> R.anim.slide_in_left
            "login" -> R.anim.slide_in_down
            else -> R.anim.slide_in_left // Nilai default jika arah tidak valid
        }

        val animOut = when (direction) {
            "search" -> R.anim.slide_out_right
            "home" -> R.anim.slide_out_right
            "login" -> R.anim.slide_out_up
            else -> R.anim.slide_out_right // Nilai default jika arah tidak valid
        }

        val intent = when (direction) {
            "search" -> Intent(this, ImgdetectActivity::class.java)
            "home" -> Intent(this, BerandaActivity::class.java)
            "login" -> Intent(this, LoginActivity::class.java)
            else -> Intent(this, ProfilActivity::class.java) // Activity default jika arah tidak valid
        }

        startActivity(intent)
        finish()
        overridePendingTransition(animIn, animOut)
    }
}