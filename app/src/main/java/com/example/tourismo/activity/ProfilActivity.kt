package com.example.tourismo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityProfilBinding

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityProfilBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener { pindahActivity("home") }
        binding.btnSearch.setOnClickListener { pindahActivity("search") }

        val color = ContextCompat.getColor(this, R.color.dasar)
        binding.btnProfile.setColorFilter(color)
    }
    private fun pindahActivity(direction: String) {
        val intent: Intent = when (direction) {
            "search" -> Intent(this, Upload_activity::class.java)
            "home" -> Intent(this, GoActivity::class.java)
            else -> Intent(this, GoActivity::class.java) // Activity default jika arah tidak valid
        }
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
    }
}