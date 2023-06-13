package com.example.tourismo.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.ApiResponse
import com.example.tourismo.api.response.UploadResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

//class UploadViewModel : ViewModel() {
//
//    private val apiService: ApiEndpoint = RetrofitClient.apiInstance
//    private val errorMessage = MutableLiveData<String>()
//    private val _selectedImageUri = MutableLiveData<Uri>()
//    val selectedImageUri: LiveData<Uri>
//        get() = _selectedImageUri
//
//
//
//    val uploadStatus = MutableLiveData<Boolean>()
//
//
//
//
//
//    fun selectImage(imageUri: Uri) {
//
//        _selectedImageUri.value = imageUri
//    }
//    fun uploadPhoto(imageUri: Uri) {
//
////        val requestFile = imageUri.("/".to)
////        val fileMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
////            "file",
////            file
////        )
//        val file = File(imageUri.path)
//        val requestFile: RequestBody =
//            RequestBody.create(MediaType.parse("multipart/form-data"), file)
//        val body: MultipartBody.Part =
//            MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//        apiService.uploadPhoto(body).enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    Log.d("VM Upload", "respons = $apiResponse")
//                    val uid = apiResponse?.uid
//                    Log.d("VM upload", "apakah uid kosong ? ${uid.isNullOrEmpty()}")
//                    if (uid.isNullOrEmpty()){
//                        val errmessage = apiResponse?.errorCode
//                        errorMessage.value = errmessage
//                        Log.d("VM upload", "response tidak terdapat uid, dan response errornya dalah $errmessage")
//                        Log.d("VM register", "upload Gagal")
//                        uploadStatus.value = false
//                    }else{
//                        Log.d("VM register", "Register Sukses")
//                        uploadStatus.value = true
//                    }
//                } else
//                {
//                    // Tangkap pesan error dari response.errorBody() sebagai string
//                    val errorResponse: String? = response.errorBody()?.string()
//                    Log.d("VM upload", "Request failed "+ errorResponse.toString())
//                  uploadStatus.value = false
//                    errorMessage.value = errorResponse.toString()
//
//                }
//            }
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                Log.d("VM Upload on failure", "Request failed: ${t.message}")
//                uploadStatus.value = false
//                errorMessage.value = t.message.toString()
//            }
//        })
//
//
//
//
////
////        val BASE_URL = "https://rentalzeus.000webhostapp.com/"
////            val file = File(imageUri.path)
////            val requestFile: RequestBody =
////                RequestBody.create(MediaType.parse("image/*"), file)
////            val body: MultipartBody.Part =
////                MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//
//        // Create Retrofit instance and interface
////        val retrofit = Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .build()
////        val apiService = retrofit.create(ApiEndpoint::class.java)
////
////        // Make the API request
////        val call = apiService.uploadPhoto(body)
////        call.enqueue(object : Callback<ApiResponse> {
////            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
////                // Handle the API response
////                if (response.isSuccessful) {
////                    val apiResponse = response.body()
////                    // Handle the successful response
////                   Log.d("Upload VM", "Berhasil")
////                    if (apiResponse != null) {
////                        // Berhasil mengunggah foto
//////                        val imageUrl = apiResponse.imageUrl
////                        // Lakukan sesuatu dengan URL gambar, misalnya tampilkan ke pengguna atau simpan dalam objek data lainnya
////                        // ...
////                    } else {
////                        // Tangani situasi ketika respons tidak sesuai dengan yang diharapkan
////                        // Misalnya, respons body kosong atau data yang hilang
////                    }
////                } else {
////                    // Tangani respons yang tidak berhasil
////                    // Misalnya, error server, error validasi, atau error lainnya
////                    val errorBody = response.errorBody()?.string()
////                    // Lakukan sesuatu dengan error body, misalnya tampilkan pesan kesalahan kepada pengguna
////                    // ...
////                }
////            }
////
////            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
////                // Tangani ketika permintaan gagal, misalnya koneksi terputus atau kesalahan jaringan
////                // Lakukan sesuatu dengan Throwable, misalnya tampilkan pesan kesalahan kepada pengguna
////                // ...
////            }
////        })
//
//
//
//
//
//
//
//
//
//
////            apiService.uploadPhoto(body).enqueue(object : Callback<ApiResponse> {
////                override fun onResponse(
////                    call: Call<ApiResponse>,
////                    response: Response<ApiResponse>
////                ) {
////                    if (response.isSuccessful) {
////                        // Handle successful upload response
////                        _uploadStatus.value = true
////                    } else {
////                        // Handle unsuccessful upload response
////                        _uploadStatus.value = false
////                    }
////                }
////
////                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
////                    // Handle failure
////                    _uploadStatus.value = false
////                }
////            })
//
//    }
//}


class UploadViewModel : ViewModel() {

    private val apiService: ApiEndpoint = RetrofitClient.apiInstance
    private val errorMessage = MutableLiveData<String>()
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
                    val apiResponse = response.body()
                    Log.d("VM Upload", "response = $apiResponse")
                    val lokasi = apiResponse?.Lokasi
                    Log.d("VM Upload", "Is uid empty? ${lokasi.isNullOrEmpty()}")
                    if (lokasi.isNullOrEmpty()) {
                        val errMessage = apiResponse?.error
                        errorMessage.value = errMessage
                        Log.d("VM Upload", "Response doesn't contain uid, and the error message is $errMessage")
                        uploadStatus.value = false
                    } else {
                        Log.d("VM Upload", "Upload Successful")
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
                Log.d("VM Upload", "Request failed: ${t.message}")
                uploadStatus.value = false
                errorMessage.value = t.message.toString()
            }
        })
    }
}