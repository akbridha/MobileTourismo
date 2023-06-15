package com.example.tourismo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.TiketResponse
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class FindtickViewModel : ViewModel() {
    private val searchStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    private val apiService: ApiEndpoint = RetrofitClient.apiInstance
    private val tiketResponseLiveData = MutableLiveData<TiketResponse>()

    fun searchTickets(departure: String, arrival: String, type: String, date: String ,accessToken: String) {
        val requestBody = RequestBody.create(
            MediaType.parse("application/json"), """
            {
                "departure": "$departure",
                "arrival": "$arrival",
                "tipe": "$type",
                "date": "$date"
            }
            """.trimIndent()
        )

        apiService.searchTickets(accessToken, requestBody).enqueue(object : Callback<TiketResponse> {
            override fun onResponse(call: Call<TiketResponse>, response: Response<TiketResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("FindtickViewModel", "ResponseIsSuccess data: $responseData")
                    tiketResponseLiveData.value = responseData
                    searchStatus.value = true
                } else {
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("FindtickViewModel", "ResponseNotSucces: $errorResponse")
                    searchStatus.value = false
                    errorMessage.value = errorResponse.toString()
                }
            }

            override fun onFailure(call: Call<TiketResponse>, t: Throwable) {
                Log.d("FindtickViewModel", "Request failed: ${t.message}")
                searchStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })
    }

    fun getSearchStatus(): LiveData<Boolean> {
        return searchStatus
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getTiketResponseLiveData(): LiveData<TiketResponse> {
        return tiketResponseLiveData
    }
}