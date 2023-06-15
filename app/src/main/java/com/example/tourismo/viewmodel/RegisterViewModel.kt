package com.example.tourismo.viewmodel

import RetrofitClient
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.ApiResponse
import com.example.tourismo.api.response.RegisterResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel(){
    private val registerStatus = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    private val apiService: ApiEndpoint = RetrofitClient.apiInstance


    fun registerUser(email: String, password: String) {

        Log.d("RegisterAct", "sendatatoapi")
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

        apiService.registerUser(requestBody).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("VM register", "respons = $apiResponse")
                    val uid = apiResponse?.uid
                    Log.d("VM register", "apakah uid kosong ? ${uid.isNullOrEmpty()}")
                    if (uid.isNullOrEmpty()){
                        val errmessage = apiResponse?.errorCode
                        errorMessage.value = errmessage
                        Log.d("VM Register User", "response tidak terdapat uid, dan response errornya dalah $errmessage")
                    Log.d("VM register", "Register Gagal")
                        registerStatus.value = false
                    }else{
                    Log.d("VM register", "Register Sukses")
                        registerStatus.value = true
                    }
                } else
                {
                    // Tangkap pesan error dari response.errorBody() sebagai string
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("VM Regis", "Request failed "+ errorResponse.toString())
                    registerStatus.value = false
                    errorMessage.value = errorResponse.toString()

                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("VM Register", "Request failed: ${t.message}")
                registerStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })

    }

    fun getRegisterStatus(): LiveData<Boolean>{
        return  registerStatus
    }
    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}

//         RetrofitClient.apiInstance
//             .registerUser(email, password)
//             .enqueue(object : Callback<RegisterResponse> {
//
//                 override fun onResponse(
//                     call: Call<RegisterResponse>,
//                     response: Response<RegisterResponse>
//                 ) {
//                     Log.d("Vm cari user", "onResponse"+response.toString())
//                     if (response.isSuccessful){
//                         Log.d("response","is success")
//                     }else{
//                         Log.d("response","is Fail "+response.body())
//                     }
//                 }
//                 override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                     Log.d("onFailure", t.message.toString())
//                 }
//             }
//         )
