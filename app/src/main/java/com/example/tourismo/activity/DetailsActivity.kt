package com.example.tourismo.activity

import RetrofitClient
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.example.tourismo.R
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.dataclass.TouristDestinationDetails
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.google.gson.JsonObject
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody

class DetailsActivity : AppCompatActivity() {
    private val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3YmFiYWFiYTEwNWFkZDZiM2ZiYjlmZjNmZjVmZTNkY2E0Y2VkYTEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2Fwc3RvbmUtYzIzLXBzMTA4IiwiYXVkIjoiY2Fwc3RvbmUtYzIzLXBzMTA4IiwiYXV0aF90aW1lIjoxNjg2ODQ3Mjc3LCJ1c2VyX2lkIjoiVXhrMjhvNVhDYk9hYmhyeHlydnNObWZ2Tnp0MiIsInN1YiI6IlV4azI4bzVYQ2JPYWJocnh5cnZzTm1mdk56dDIiLCJpYXQiOjE2ODY4NDcyNzcsImV4cCI6MTY4Njg1MDg3NywiZW1haWwiOiJkYXIxQGRhdy5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiZGFyMUBkYXcuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.jM45mrWNBkScRldZiap4sxyIXTErh9xdMZWa5RQfJemcuZIivSq7xWjNWiwOe4E7vd6KIL7mpmG_EP7pOG2aXUh7StfmV7ngvfFP1Hq4sHEbhn0rSOwxLx9Rwnq6xcl80s9ZrDRN-jP_xBL3e4PIzKDhY288Wz58IeKWXCLHn9-IM36VqUR5rcYJEYuD3N4MT-k1ljqBwjN7KlbKRo13EW3kwx_GqYsFAhKxA2qE6YtFP4qw6cWZSN_N-7beFEUivgElE2Ti58GO1QUmtL6bC-ySouQ9jAPu9vjID4lDzL6wKxUX-wKRNXWySg1eGvaywuhEMIme81yKzwLp_qyqEg"
    private val apiHelper: ApiEndpoint = RetrofitClient.apiInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destinationName = intent.getStringExtra("destinationName") // Get the destination id from the intent

        if (destinationName != null) {
            fetchTouristDestinationDetails(destinationName) {
                setContent {
                    CustomTheme {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Column {
                                TopAppBar(
                                    navigationIcon = {
                                        IconButton(onClick = { onBackPressed() }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color(0xFF807BFE)
                                            )
                                        }
                                    },
                                    backgroundColor = Color.White,
                                    title = { Text(formatTitle(destinationName), style = MaterialTheme.typography.h5) }
                                )
                                TouristDestinationDetailsView(destinationDetails = it)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TouristDestinationDetailsHeader(destinationDetails: TouristDestinationDetails) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = destinationDetails.lokasi,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp)) // Add spacer for vertical spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Opening Hours",
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = destinationDetails.jamBuka,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }





    @Composable
    fun TouristDestinationDetailsView(destinationDetails: TouristDestinationDetails) {
        val selectedTab = remember { mutableStateOf(0) }
        val tabTitles = listOf("Description", "Short History", "Fun Fact")

        LazyColumn {
            item {
                // Image loading logic using Coil-Compose
                val painter = rememberImagePainter(
                    data = destinationDetails.url,
                    builder = {
                        crossfade(true)
                        size(1000)
                        scale(Scale.FILL) // Scale the image to fill the given dimensions
                    }
                )

                Image(
                    painter = painter,
                    contentDescription = "Destination Image",
                    modifier = Modifier.fillMaxWidth()
                        .size(300.dp)
                )

                TouristDestinationDetailsHeader(destinationDetails)

                TabRow(
                    selectedTabIndex = selectedTab.value,
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = selectedTab.value == index,
                            onClick = { selectedTab.value = index }
                        )
                    }
                }
            }

            item {
                when (selectedTab.value) {
                    0 -> {
                        Text(text = destinationDetails.deskripsi, style = MaterialTheme.typography.body1, modifier = Modifier.padding(16.dp))
                    }
                    1 -> {
                        Text(text = destinationDetails.historiSingkat, style = MaterialTheme.typography.body1, modifier = Modifier.padding(16.dp))
                    }
                    2 -> {
                        Text(text = destinationDetails.funFact, style = MaterialTheme.typography.body1, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }






    private fun fetchTouristDestinationDetails(destinationName: String, onResult: (TouristDestinationDetails) -> Unit) {
        val param = JsonObject().apply {
            addProperty("nama", destinationName)
        }

        apiHelper.getSpecificTourist(param).enqueue(object : Callback<TouristDestinationDetails> {
            override fun onResponse(
                call: Call<TouristDestinationDetails>,
                response: Response<TouristDestinationDetails>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { destinationDetails ->
                        onResult(destinationDetails)
                    }
                }
            }

            override fun onFailure(call: Call<TouristDestinationDetails>, t: Throwable) {
                // Handle error
                Log.d("Beranda act","getDestination fail")
            }
        })
    }

    @Composable
    fun CustomTheme(content: @Composable () -> Unit) {
        val typography = Typography(
            h6 = TextStyle(
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontSize = 20.sp,
                color = Color(0xFF807BFE)
            ),
            body2 = TextStyle(
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 14.sp
            ),
            h5 = TextStyle(
                fontFamily = FontFamily(Font(R.font.raleway)),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF000000)
            ),
            body1 = TextStyle(
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 16.sp
            ),
        )

        MaterialTheme(
            typography = typography,
            content = content
        )
    }

    fun formatTitle(title: String): String {
        val formattedTitle = StringBuilder()
        val words = title.split("(?<=.)(?=\\p{Lu})".toRegex())

        words.forEachIndexed { index, word ->
            if (index > 0) {
                formattedTitle.append(" ")
            }
            formattedTitle.append(word.capitalize())
        }

        return formattedTitle.toString()
    }
}
