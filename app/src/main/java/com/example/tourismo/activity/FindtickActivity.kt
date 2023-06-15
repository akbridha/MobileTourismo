package com.example.tourismo.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tourismo.R
import com.example.tourismo.databinding.ActivityFindtickBinding
import com.example.tourismo.viewmodel.FindtickViewModel
import com.google.gson.Gson
import java.util.*

class FindtickActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFindtickBinding
    private lateinit var selectedDate: String
    private lateinit var viewModel : FindtickViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindtickBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
        viewModel = ViewModelProvider(this).get(FindtickViewModel::class.java)


        // Tambahkan listener untuk spinner
        binding.spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Dapatkan item yang dipilih dari spinner keberangkatan
                val departure = parent?.getItemAtPosition(position).toString()
                // Lakukan sesuatu dengan nilai departure
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada yang dipilih
            }
        }

        binding.spinnerDestination.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Dapatkan item yang dipilih dari spinner tujuan
                val arrival = parent?.getItemAtPosition(position).toString()
                // Lakukan sesuatu dengan nilai arrival
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada yang dipilih
            }
        }

        // Tambahkan listener untuk TextView tanggal
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }



        binding.btnFind.setOnClickListener {
            val departure = binding.spinnerFrom.selectedItem.toString()
            val arrival = binding.spinnerDestination.selectedItem.toString()
            val type = "bisnis" // Ubah sesuai dengan tipe yang diinginkan
            val date = selectedDate

            val accessToken = "Bearer "+sharedPreferences.getString("accessToken", "kosng")
            viewModel.searchTickets(departure, arrival, type, date, accessToken)
        }

        observeSearchStatus()
        observeHasilTiket()
        observeErrorMessage()
    }

    private fun observeHasilTiket() {
        viewModel.getTiketResponseLiveData().observe(this) { tiketResponse ->
//            binding.tvHasilTiket.setText(Gson().toJson(tiketResponse))
            Log.d("FindtickActivity", Gson().toJson(tiketResponse))
            // Buat Intent
            val intent = Intent(this, TiketActivity::class.java)

            val  stringResponse :String = (Gson().toJson(tiketResponse))
            // Sertakan data tiketResponse sebagai ekstra pada Intent
            intent.putExtra("tiketResponse", stringResponse)

            // Jalankan Activity selanjutnya
            startActivity(intent)
        }
    }


    private fun observeSearchStatus() {
        viewModel.getSearchStatus().observe(this) { searchStatus ->
            if (searchStatus) {
                // Data berhasil dikirim ke API
                // Lakukan sesuatu setelah mendapatkan respon dari API
            } else {
                // Data gagal dikirim ke API
                // Lakukan sesuatu ketika terjadi kesalahan
            }
        }
    }

    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            // Lakukan sesuatu dengan pesan errorMessage di sini
            Log.d("ImgDetActivity", "Error message: $errorMessage")

            if (errorMessage.contains("{\"error\":\"Unauthorized\"}")) {
                Toast.makeText(this, "$errorMessage Silahkan Login Ulang", Toast.LENGTH_SHORT)
                    .show()

//                menghapus sharedPreferense agar User harus login ulang karena token yg ada expire
                val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                pindahActivity("login")
            } else {
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
            "home" -> Intent(this, GoActivity::class.java)
            "profil" -> Intent(this, ProfilActivity::class.java)
            "tiket" -> Intent(this, FindtickActivity::class.java)
            "login" -> Intent(this, LoginActivity::class.java)

            else -> Intent(this, GoActivity()::class.java) // Activity default jika arah tidak valid
        }

        startActivity(intent)
        finish()
        overridePendingTransition(animIn, animOut)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format tanggal yang dipilih
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            selectedDate = formattedDate

            // Tampilkan tanggal yang dipilih pada TextInputEditText
            binding.textDate.setText(formattedDate)
        }, year, month, day)

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }
}