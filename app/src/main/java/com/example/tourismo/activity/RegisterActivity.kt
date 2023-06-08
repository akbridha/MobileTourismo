package com.example.tourismo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.R
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.ApiResponse
import com.example.tourismo.databinding.ActivityRegisterBinding
import com.example.tourismo.viewmodel.RegisterViewModel
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel


    private val apiService: ApiEndpoint = RetrofitClient.apiInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            RegisterViewModel::class.java)


        binding.tvLogin.setOnClickListener{
            startActivity( Intent(this, LoginActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
        }
        binding.btnRegister.setOnClickListener{


            sendDataToApi()
//            cekdulu sdh diisi atau belum lalu cek format isinya
//            if (isAllEditTextFilled()){
//                Log.d("RegisterAct","Semua Edittext sudah diisi")
//                if(cekEdittext()){
//                    val email = binding.editTextTextEmailAddress.text.toString().trim()
//                    val password = binding.editTextTextNewPass.text.toString().trim()
//
//                    viewModel.registerUser(email,password)
//
//                    Log.d("RegisterAct","Mengirim Post Ke API")
//
//                }else{
//                    Log.d("RegisterAct","Input Password tidak sama")
//                }
//            }
//            else{
//                Log.d("RegisterAct","ada Edittext yang belum diisi")
//            }
        }
    }

    private fun cekEdittext(): Boolean {
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
                return true
            }
            else
            {
                binding.editTextTextNewPass.requestFocus()
                binding.editTextTextNewPass.error = "Password tidak sama"
                binding.editTextTextNewPassConfirm.contentDescription = "Harap isi dengan password yang sama "
                Log.d("RegisterAct", "Password tidak sesuai")
                return  false
            }

    }

    private fun isAllEditTextFilled(): Boolean {
        return !TextUtils.isEmpty(binding.editTextTextEmailAddress.text) &&
                !TextUtils.isEmpty(binding.editTextTextNewPass.text) &&
                !TextUtils.isEmpty(binding.editTextTextNewPassConfirm.text)
    }


    private fun sendDataToApi() {
        Log.d("RegisterAct", "sendatatoapi")
        val json = """
            {
                "email": "dur11@daxa.com",
                "password": "1234Af"
            }
        """.trimIndent()

        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

        apiService.registerUser(requestBody).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
//                    apiResponse?.message?.let { Log.d("Response", it) }
                    Log.d("Response", "$apiResponse")
                } else {
                    // Tangkap pesan error dari response.errorBody() sebagai string
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("Response", "Request failed "+ errorResponse.toString())
                    Log.d("Response", "Request failed "+ errorResponse.toString())
                    Log.d("Response", "Request failed "+ errorResponse.toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("Response", "Request failed: ${t.message}")
            }
        })
    }

}
