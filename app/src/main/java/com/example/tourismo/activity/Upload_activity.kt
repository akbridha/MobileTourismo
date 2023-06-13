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
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.R
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



        binding.tvHasil.visibility= View.INVISIBLE
        binding.imageViewResult.visibility= View.INVISIBLE
        binding.iconChange.visibility = View.INVISIBLE
        binding.progressBarUpload.visibility = View.INVISIBLE

        val color = ContextCompat.getColor(this, R.color.dasar)
        binding.btnSearch.setColorFilter(color)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UploadViewModel::class.java
        )


        //button Navigasi Bawahnya


        binding.btnHome.setOnClickListener{
            pindahActivity("home")
        }
        binding.btnProfile.setOnClickListener{
            pindahActivity("profil")
        }






        binding.imageView.setOnClickListener { selectPhotoFromGallery() }
        binding.iconChange.setOnClickListener{
            selectPhotoFromGallery()
            binding.tvHasil.visibility= View.INVISIBLE }
        binding.buttonFind.setOnClickListener {uploadPhoto()}
        viewModel.selectedImageFile.observe(this, Observer { file ->
            if (file != null) {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
                binding.imageViewResult.setImageBitmap(bitmap)
                binding.iconChange.visibility = View.VISIBLE
                binding.imageViewResult.visibility= View.VISIBLE
                Log.d("UploadAct","Hasil Foto ditampilkan Di UI")

            }
        })


//        Mengamati hasil dari API, jika ada perubahan maka akan dieksekusi di sini
        viewModel.lokasi.observe(this) { lokasi ->
            if (lokasi != null) {

                binding.tvHasil.visibility= View.VISIBLE
                // Tampilkan nilai lokasi di tampilan (misalnya TextView)
                binding.tvHasil.text = lokasi
            }
        }

        viewModel.progressBarVisibility.observe(this, Observer { visibility ->
            binding.progressBarUpload.visibility = visibility
        })
    }

    private fun pindahActivity(direction: String) {
        val intent: Intent = when (direction) {
            "home" -> Intent(this, GoActivity::class.java)
            "profil" -> Intent(this, ProfilActivity::class.java)
            else -> Intent(this, GoActivity::class.java) // Activity default jika arah tidak valid
        }
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in_left)
    }
    private fun uploadPhoto() {
        if (imageFile != null) {
        binding.progressBarUpload.visibility= View.VISIBLE
            Log.d("UploadAct", "Mengupload foto")
            viewModel.uploadPhoto(imageFile!!)
        } else {
            // Image is not selected, show error message
            Toast.makeText(this, "Pilih Gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
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
        if (checkPermission()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }else {
             requestPermission()
        }
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