package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tourismo.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out)
    }
}