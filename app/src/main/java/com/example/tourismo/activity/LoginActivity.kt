package com.example.tourismo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.R
import com.example.tourismo.api.response.ApiResponse
import com.example.tourismo.databinding.ActivityLoginBinding
import com.example.tourismo.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private  lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isUserLoggedIn()) {
            Log.d("LoginAct", "SF masih ada.. user sdh pernah login")
            pindahActivityBeranda()
        }else {
            Log.d("LoginAct", "SF masih Kosong.. User belum pernah login")
        }
            binding.buttonLogin.setOnClickListener {prosesLogin()}
        binding.tvRegister.setOnClickListener{ pindahActivityRegister()}

        tandaLoading(false)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoginViewModel::class.java)

        viewModel.getLoginStatus().observe(this) { isSuccess ->
            tandaLoading(false)
            if (isSuccess) {
                // Login berhasil
                Log.d("LoginAct", "Login berhasil")
                Toast.makeText(this,"Login Berhasil",Toast.LENGTH_SHORT).show()

                simpanDataUser()
            } else {
                // Login gagal
                Log.d("LoginAct", "Login gagal")
//                nampilin pesan errornya
                viewModel.getErrorMessage().observe(this) { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.d("LoginAct", "Pesan error: $errorMessage")
                }
                Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun simpanDataUser() {

        viewModel.getApiResponse().observe(this) { response ->



            Log.d("LoginACt", "hasil response email $response")
            saveResponseToSf(response)
        }

//        nyimpan ke sharenPref supaya user tidak login berkali-kali kalo sdh pernah login.
    }

    private fun saveResponseToSf(response: ApiResponse?) {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (response != null) {
            editor.putString("email", response.email)
        }
        // Tambahkan kode ini untuk menyimpan atribut lain dari response
        if (response != null) {
            editor.putString("uid", response.uid)
        }
        if (response != null) {
            editor.putBoolean("emailVerified", response.emailVerified)
        }
        // Tambahkan atribut lain yang perlu disimpan
        editor.commit()
        pindahActivityBeranda()
    }

    private fun pindahActivityBeranda() {
        startActivity(Intent(this, GoActivity::class.java))
        finish()
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
    }

    private fun pindahActivityRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
    }
    private fun prosesLogin() {

        // cekdulu sdh diisi atau belum lalu cek format isinya
        val editTexts = listOf(binding.editTextTextEmailAddress, binding.editTextTextPassword)
        var hasEmptyField = false

        for (editText in editTexts) {
            if (TextUtils.isEmpty(editText.text)) {
                editText.requestFocus()
                editText.error = "Kolom tidak diisi"
                editText.contentDescription = "Harap isi semua kolom"
                hasEmptyField = true
            }
        }

        if (!hasEmptyField) {
            Log.d("RegisterAct", "Semua Edittext sudah diisi")
            if (cekEdittext()) {
                kirimData()
                tandaLoading(true)
                Log.d("RegisterAct", "Run viewModel.registerUser(email,pass)")
            } else {
                Log.d("RegisterAct", "Input Password tidak sama")
            }
        } else {
            Log.d("RegisterAct", "Ada Edittext yang belum diisi")
        }
    }
    private fun kirimData() {
        val email = binding.editTextTextEmailAddress.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()

        viewModel.loginUser(email, password)
        Log.d("LoginAct", "Mengirim Post Ke API")
    }
    private fun cekEdittext(): Boolean {
        //Mengambil Email
        val email = binding.editTextTextEmailAddress.text.toString().trim()

        //Mencek format Email
        val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (isValidEmail) {
            Log.d("RegisterAct", "Email format valid")
            return  true
        }
        else
        {
            binding.editTextTextEmailAddress.error = "Format email tidak valid"
            Log.d("RegisterAct", "Email format invalid")
            return  false
        }


    }
    private fun isAllEditTextFilled(): Boolean {

        return !TextUtils.isEmpty(binding.editTextTextEmailAddress.text) &&
                !TextUtils.isEmpty(binding.editTextTextPassword.text)
    }

    private fun tandaLoading(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains("email")
    }
}