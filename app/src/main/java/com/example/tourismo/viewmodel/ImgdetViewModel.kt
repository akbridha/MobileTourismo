package com.example.tourismo.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.UploadResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File



class ImgdetViewModel : ViewModel() {

    private val apiService: ApiEndpoint = RetrofitClient.apiInstance
    val errorMessage = MutableLiveData<String>()
    private val hasilGambar = MutableLiveData<String>()
    val lokasi: LiveData<String>
        get() = hasilGambar

    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)

    private val _selectedImageFile = MutableLiveData<File>()
    val selectedImageFile: LiveData<File>
        get() = _selectedImageFile

    val uploadStatus = MutableLiveData<Boolean>()

    fun selectImage(imageFile: File) {
        _selectedImageFile.value = imageFile
    }

    fun uploadPhoto(imageFile: File, authHeader: String) {
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), imageFile)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        apiService.uploadPhoto(authHeader, body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    _progressBarVisibility.value = View.INVISIBLE
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("VM imageDetection", "response isSuccesful  $apiResponse")
                    val lokasi = apiResponse?.Lokasi
                    Log.d("VM imageDetection", "Is lokasi emty? ${lokasi.isNullOrEmpty()}")
                    if (lokasi.isNullOrEmpty()) {
                        val errMessage = apiResponse?.error
                        errorMessage.value = errMessage
                        Log.d("VM imageDetection", "Response doesn't contain lokasi, and the error message is $errMessage")
                        uploadStatus.value = false
                    } else {
                        Log.d("VM imageDetection", "Upload Successful")
                        hasilGambar.value = lokasi
                        uploadStatus.value = true
                    }
                } else {

                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("VM imageDetection", "Request isNOtSuccesfl: $errorResponse")
                    uploadStatus.value = false
                    errorMessage.value = errorResponse.toString()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _progressBarVisibility.value = View.INVISIBLE
                Log.d("VM imageDetection", "onFailure Request api fail: ${t.message}")
                uploadStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })
    }
}