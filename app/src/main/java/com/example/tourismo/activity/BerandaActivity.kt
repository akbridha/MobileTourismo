package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityBerandaBinding
class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}