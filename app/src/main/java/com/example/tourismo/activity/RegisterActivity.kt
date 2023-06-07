package com.example.tourismo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.tourismo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener{


//            cekdulu sdh diisi atau belum lalu cek format isinya
            if (isAllEditTextFilled()){
                Log.d("RegisterAct","Semua Edittext sudah diisi")
                cekEdittext()}
            else{
                Log.d("RegisterAct","ada Edittext yang belum diisi")
            }
        }
    }

    private fun cekEdittext() {
            //Mengambil Email
            val email = binding.editTextTextEmailAddress.text.toString().trim()

            //Mencek format Email
            val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (isValidEmail) {      Log.d("RegisterAct", "Email format valid")      } else {
                binding.editTextTextEmailAddress.error = "Format email tidak valid"
                Log.d("RegisterAct", "Email format invalid") }


//              Mengambil input edt 1 dan 2
            val text1 = binding.editTextTextNewPass.text.toString().trim()
            val text2 = binding.editTextTextNewPassConfirm.text.toString().trim()

//              Mencek 2 editText Input sama atau tidak
            if (text1 == text2) {
                Log.d("RegisterAct", "Password sesuai")
                  } else {
                binding.editTextTextNewPass.requestFocus()
                binding.editTextTextNewPass.error = "Email tidak sama"
                binding.editTextTextNewPassConfirm.contentDescription = "Harap isi dengan teks yang sama dengan EditText 1"
                Log.d("RegisterAct", "Password tidak sesuai")
            }






    }

    private fun isAllEditTextFilled(): Boolean {
        return !TextUtils.isEmpty(binding.editTextTextEmailAddress.text) &&
                !TextUtils.isEmpty(binding.editTextTextNewPass.text) &&
                !TextUtils.isEmpty(binding.editTextTextNewPassConfirm.text)
    }

}
