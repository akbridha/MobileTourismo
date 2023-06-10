package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tourismo.databinding.ActivityGoBinding

class GoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGoBinding
//    private lateinit var viewModel :
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}