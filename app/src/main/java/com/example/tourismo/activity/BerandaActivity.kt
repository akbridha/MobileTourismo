package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityBerandaBinding
import com.example.tourismo.databinding.ActivityUploadBinding

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_beranda)
    }
}