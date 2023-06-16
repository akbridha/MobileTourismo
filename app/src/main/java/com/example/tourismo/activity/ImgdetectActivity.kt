package com.example.tourismo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityImgdetBinding


import com.example.tourismo.viewmodel.ImgdetViewModel
import java.io.File
import java.io.IOException




class ImgdetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImgdetBinding
    private var imageFile: File? = null
    private lateinit var viewModel: ImgdetViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgdetBinding.inflate(layoutInflater)
        setContentView(binding.root)

    sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)

        Log.d(" IMGdetAct", "Hasil token ${sharedPreferences.getString("accessToken", "kosng")}")

        binding.tvHasil.visibility= View.INVISIBLE
        binding.imageViewResult.visibility= View.INVISIBLE
        binding.iconChange.visibility = View.INVISIBLE
        binding.progressBarUpload.visibility = View.INVISIBLE

        val color = ContextCompat.getColor(this, R.color.dasar)
        binding.btnSearch.setColorFilter(color)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ImgdetViewModel::class.java
        )


        //button Navigasi Bawahnya


        binding.btnHome.setOnClickListener{ pindahActivity("home")        }
        binding.btnProfile.setOnClickListener{pindahActivity("profil")        }
//        binding.buttonTiket.setOnClickListener {pindahActivity("tiket") }

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

        viewModel.errorMessage.observe(this) { errorMessage ->
            // Lakukan sesuatu dengan pesan errorMessage di sini
            Log.d("ImgDetActivity", "Error message: $errorMessage")

            if (errorMessage.contains("{\"error\":\"Unauthorized\"}")){
                val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                Toast.makeText(this, "$errorMessage Silahkan Login Ulang", Toast.LENGTH_SHORT)
                    .show()
                pindahActivity("login")
            }else{
                Toast.makeText(this, "$errorMessage Koneksi Bermasalah", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun pindahActivity(direction: String) {
        val animIn = when (direction) {
            "home" -> R.anim.slide_in_left
            "profil" -> R.anim.slide_in_right
            "tiket" -> R.anim.slide_in_up
            "login" -> R.anim.slide_in_up

            else -> R.anim.slide_in_left // Nilai default jika arah tidak valid
        }

        val animOut = when (direction) {
            "home" -> R.anim.slide_out_right
            "profil" -> R.anim.slide_out_left
            "tiket" -> R.anim.slide_out_down
            "login" -> R.anim.slide_out_down

            else -> R.anim.slide_out_right // Nilai default jika arah tidak valid
        }

        val intent = when (direction) {
            "home" -> Intent(this, BerandaActivity::class.java)
            "profil" -> Intent(this, ProfilActivity::class.java)
            "tiket" -> Intent(this, FindtickActivity::class.java)
            "login" -> Intent(this, LoginActivity::class.java)

            else -> Intent(this, ImgdetectActivity()::class.java) // Activity default jika arah tidak valid
        }

        startActivity(intent)
        finish()
        overridePendingTransition(animIn, animOut)
    }
    private fun uploadPhoto() {
        if (imageFile != null) {
        binding.progressBarUpload.visibility= View.VISIBLE
            Log.d("UploadAct", "Mengupload foto")
            val accessToken = "Bearer "+sharedPreferences.getString("accessToken", "kosng")
            viewModel.uploadPhoto(imageFile!!, accessToken)
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