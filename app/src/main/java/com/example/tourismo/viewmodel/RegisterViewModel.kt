package com.example.tourismo.viewmodel

import RetrofitClient
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel(){
    private val _registerResult = MutableLiveData<Boolean>()


    fun registerUser(email: String, password: String) {

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
    }
}