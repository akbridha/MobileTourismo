package com.example.tourismo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.response.ApiResponse


import com.example.tourismo.databinding.ActivityUploadBinding
import com.example.tourismo.viewmodel.RegisterViewModel
import com.example.tourismo.viewmodel.UploadViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException

//class Upload_activity : AppCompatActivity() {
//    private lateinit var binding: ActivityUploadBinding
//    private var imageUri: Uri? = null
//    private var imageFile: File? = null
//    private lateinit var viewModel: UploadViewModel
//
//    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2
//
//    companion object {
//        private const val REQUEST_IMAGE_PICK = 1
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityUploadBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
//           UploadViewModel::class.java)
//
//        val buttonSelectPhoto: Button = binding.buttonSelectPhoto
//        val buttonUpload: Button = binding.buttonUpload
//
//        buttonSelectPhoto.setOnClickListener {
//            if (checkPermission()) {
//                selectPhotoFromGallery()
//            } else {
//                requestPermission()
//            }
//        }
//        buttonUpload.setOnClickListener {
//            // Implement your upload logic here
//            if (imageUri != null) {
//                Log.d("UploadAct","Mengupload foto")
//                viewModel.uploadPhoto(imageUri!!)
//            } else {
//                // Image is not selected, show error message
//                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        viewModel.selectedImageUri.observe(this, Observer { imageUri ->
//            if (imageUri != null){
//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
//                binding.imageView.setImageBitmap(bitmap)
//            }
//        })
//
//    }
//
//
//
//
//
//
//
//    private fun checkPermission(): Boolean {
//        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//        return result == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST
//        )
//    }
//
//    private fun selectPhotoFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        startActivityForResult(intent, REQUEST_IMAGE_PICK)
//    }
//
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
//            imageUri = data?.data
//            if (imageUri != null) {
//               viewModel.selectImage(imageUri!!)
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    selectPhotoFromGallery()
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Permission Denied. Cannot access gallery.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }
//
//
//}


class Upload_activity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var imageFile: File? = null
    private lateinit var viewModel: UploadViewModel

    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UploadViewModel::class.java
        )

        val buttonSelectPhoto: Button = binding.buttonSelectPhoto
        val buttonUpload: Button = binding.buttonUpload

        buttonSelectPhoto.setOnClickListener {
            if (checkPermission()) {
                selectPhotoFromGallery()
            } else {
                requestPermission()
            }
        }
        buttonUpload.setOnClickListener {
            // Implement your upload logic here
            if (imageFile != null) {
                Log.d("UploadAct", "Mengupload foto")
                viewModel.uploadPhoto(imageFile!!)
            } else {
                // Image is not selected, show error message
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.selectedImageFile.observe(this, Observer { file ->
            if (file != null) {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
                binding.imageView.setImageBitmap(bitmap)
            }
        })
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST
        )
    }

    private fun selectPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    imageFile = createImageFile()
                    inputStream?.let { input ->
                        imageFile?.let { file ->
                            file.copyInputStreamToFile(input)
                            viewModel.selectImage(file)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhotoFromGallery()
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied. Cannot access gallery.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val storageDir: File? = getExternalFilesDir(null)
        return try {
            File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun File.copyInputStreamToFile(inputStream: java.io.InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }
}