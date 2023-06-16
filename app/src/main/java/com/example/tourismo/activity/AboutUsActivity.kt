package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.example.tourismo.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAbout.setGravity(Gravity.FILL);
        binding.tvAbout.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
    }
}