package com.example.tourismo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.ApiResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel(){
    private val loginStatus = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    private val apiService: ApiEndpoint = RetrofitClient.apiInstance




    fun loginUser(email: String, password: String) {

        Log.d("LoginAct", "sendatatoapi")
        val json = """
        {
            "email": "$email",
            "password": "$password"
        }
    """.trimIndent()

        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

        apiService.loginUser(requestBody).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("VM login", "respons = $apiResponse")
                    val uid = apiResponse?.uid
                    Log.d("VM login", "apakah uid kosong ? ${uid.isNullOrEmpty()}")
                    if (uid.isNullOrEmpty()){
                        val errmessage = apiResponse?.errorCode
                        errorMessage.value = errmessage
                        Log.d("VM Login User", "response tidak terdapat uid, dan response errornya dalah $errmessage")
                        Log.d("VM login", "Login Gagal")
                        loginStatus.value = false
                    }else{
                        Log.d("VM login", "Login Sukses")
                        loginStatus.value = true
                    }
                } else {
                    // Tangkap pesan error dari response.errorBody() sebagai string
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("VM Login", "Request failed "+ errorResponse.toString())
                    loginStatus.value = false
                    errorMessage.value = errorResponse.toString()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("VM Login", "Request failed: ${t.message}")
                loginStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })

    }

    fun getLoginStatus(): LiveData<Boolean> {
        return loginStatus
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}
