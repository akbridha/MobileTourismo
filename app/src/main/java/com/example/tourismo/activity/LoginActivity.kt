package com.example.tourismo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.tvRegister.setOnClickListener{
           startActivity( Intent(this, RegisterActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
        }





    }

}