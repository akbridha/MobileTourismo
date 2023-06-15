package com.example.tourismo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.dataclass.TouristDestination
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel(){
    private val loginStatus = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    private val apiService: ApiEndpoint = RetrofitClient.apiInstance

    private val apiResponse = MutableLiveData<List<TouristDestination>>()



    fun getAllTourist() {

        Log.d("LoginAct", "sendatatoapi")



        apiService.getAllTourist().enqueue(object : Callback<List<TouristDestination>> {
            override fun onResponse(
                call: Call<List<TouristDestination>>,
                response: Response<List<TouristDestination>>
            ) {
                if (response.isSuccessful) {
                    val onResponseData = response.body()
                    Log.d("VM login", "respons = $onResponseData")

//                    Log.d("VM login", "apakah uid kosong ? ${ onResponseData?.nama.isNullOrEmpty()}")
//                    if ( onResponseData?.nama.isNullOrEmpty()){
//                        val errmessage = onResponseData?.errorCode
//                        errorMessage.value = errmessage
//                        Log.d("VM Login User", "response tidak terdapat uid, dan response errornya dalah $errmessage")
//                        Log.d("VM login", "Login Gagal")
//                        loginStatus.value = false
//                    }else{
//                        Log.d("VM login", "Login Sukses")
//                        loginStatus.value = true
//                        setApiResponse(onResponseData)
//                    }
                } else {
                    // Tangkap pesan error dari response.errorBody() sebagai string
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("VM Login", "Request failed "+ errorResponse.toString())
                    loginStatus.value = false
                    errorMessage.value = errorResponse.toString()
                }
            }

            override fun onFailure(call: Call<List<TouristDestination>>, t: Throwable) {
                Log.d("VM Login", "Request failed: ${t.message}")
                loginStatus.value = false
                errorMessage.value = t.message.toString()
            }

//            override fun onResponse(
//                call: Call<List<TouristDestination>>,
//                response: Response<List<TouristDestination>>
//            ) {
//                TODO("Not yet implemented")
//            }
        })

    }

    private fun setApiResponse(onResponseData: TouristDestination?) {

//        apiResponse.value = onResponseData
    }



    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

//    fun getApiResponse(): LiveData<TouristDestination> {
//        return apiResponse
//    }
}
