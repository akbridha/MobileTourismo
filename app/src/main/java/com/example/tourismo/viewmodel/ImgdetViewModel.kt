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
    private val errorMessage = MutableLiveData<String>()
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

    fun uploadPhoto(imageFile: File) {
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), imageFile)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        apiService.uploadPhoto(body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    _progressBarVisibility.value = View.INVISIBLE
                    val apiResponse = response.body()
                    Log.d("VM Upload", "response = $apiResponse")
                    val lokasi = apiResponse?.Lokasi
                    Log.d("VM Upload", "Is lokasi emty? ${lokasi.isNullOrEmpty()}")
                    if (lokasi.isNullOrEmpty()) {
                        val errMessage = apiResponse?.error
                        errorMessage.value = errMessage
                        Log.d("VM Upload", "Response doesn't contain lokasi, and the error message is $errMessage")
                        uploadStatus.value = false
                    } else {
                        Log.d("VM Upload", "Upload Successful")
                        hasilGambar.value = lokasi
                        uploadStatus.value = true
                    }
                } else {
                    val errorResponse: String? = response.errorBody()?.string()
                    Log.d("VM Upload", "Request failed: $errorResponse")
                    uploadStatus.value = false
                    errorMessage.value = errorResponse.toString()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _progressBarVisibility.value = View.INVISIBLE
                Log.d("VM Upload", "Request failed: ${t.message}")
                uploadStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })
    }
}