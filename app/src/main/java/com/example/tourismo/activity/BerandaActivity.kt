package com.example.tourismo.activity

import RetrofitClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.tourismo.R
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.HeaderInterceptor
import com.example.tourismo.dataclass.TouristDestination
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BerandaActivity : AppCompatActivity() {

    private val apiHelper : ApiEndpoint = RetrofitClient.apiInstance
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var accessToken : String

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
    accessToken = "Bearer "+sharedPreferences.getString("accessToken", "kosng")
        val context =this
        fetchTouristDestinations(context){ destinations ->
            setContent {
                CustomTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        HomePage(destinations)
                    }
                }
            }
        }
    }

    @Composable
    fun DestinationItem(
        destination: TouristDestination,
        isImageOnRight: Boolean,
        function: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(135.dp)
                .clickable(onClick = function),
            horizontalArrangement = if (isImageOnRight) Arrangement.End else Arrangement.Start
        ) {
            if (!isImageOnRight) {
                Image(
                    painter = rememberImagePainter(
                        data = destination.url,
                        builder = {
                            size(200) // Set the desired size to 100 pixels
                            scale(Scale.FILL) // Scale the image to fill the given dimensions
                        }
                    ),
                    contentDescription = destination.nama,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(165.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(Modifier.width(24.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(formatTitle(destination.nama), style = MaterialTheme.typography.h6)
                Text(
                    destination.deskripsi,
                    style = MaterialTheme.typography.body2,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isImageOnRight) {
                Spacer(Modifier.width(24.dp))
                Image(
                    painter = rememberImagePainter(
                        data = destination.url,
                        builder = {
                            size(200) // Set the desired size to 100 pixels
                            scale(Scale.FILL) // Scale the image to fill the given dimensions
                        }
                    ),
                    contentDescription = destination.nama,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(165.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }



    @Composable

    fun HomePage(destinations: List<TouristDestination>) {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp)
                    .verticalScroll(scrollState)
            ) {

                Button(
                    onClick = {
                        val intent = Intent(context, FindtickActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.planebulk),
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(25.dp),
                        tint = colorResource(id = R.color.dasar)
                    )
                    Text("Explore Flight", style = MaterialTheme.typography.h5,)
                }
                destinations.forEachIndexed { index, destination ->
                    DestinationItem(destination, isImageOnRight = index % 2 != 1) {
                        val intent = Intent(context, DetailsActivity::class.java)
                        intent.putExtra(
                            "destinationName",
                            destination.nama
                        ) // Pass the destination id to DetailsActivity
                        context.startActivity(intent)
                    }
                }
            }

            Surface(
                elevation = 4.dp,
                shape = RoundedCornerShape(100.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(67.dp)
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.grey_border),
                        shape = RoundedCornerShape(100.dp)
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .padding(bottom = 10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Home",
                            modifier = Modifier
                                .size(25.dp),
                            tint = colorResource(id = R.color.dasar)
                        )
                        Text(
                            text = "Home",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 25.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .clickable {
                                val intent =
                                    Intent(this@BerandaActivity, ImgdetectActivity::class.java)
                                startActivity(intent)
                                finish()
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Search",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 25.dp),

                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .clickable {
                                val intent =
                                    Intent(this@BerandaActivity, ProfilActivity::class.java)
                                startActivity(intent)
                                finish()
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "Profile",
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Profile",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 25.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        }
    }

//    @Preview
//    @Composable
//    fun BoxPreview() {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 4.dp)
//                .height(80.dp)
//                .border(
//                    width = 1.dp,
//                    color = colorResource(R.color.grey_border),
//                    shape = RoundedCornerShape(100.dp)
//                ),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//            Row(
//                modifier = Modifier
//                    .padding(horizontal = 5.dp)
//                    .padding(bottom =15.dp)
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(painter = painterResource(id = R.drawable.home),
//                        contentDescription = "Home", modifier = Modifier.size(45.dp))
//                    Text(
//                        text = "Home",
//                        modifier = Modifier
//                            .padding(horizontal = 25.dp),
//                        textAlign = TextAlign.Center
//                    )
//                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(painter = painterResource(id = R.drawable.search),
//                        contentDescription = "Search", modifier = Modifier.size(45.dp))
//                    Text(
//                        text = "Search",
//                        modifier = Modifier
//                            .padding(horizontal = 25.dp),
//
//                        textAlign = TextAlign.Center
//                    )
//                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(painter = painterResource(id = R.drawable.person),
//                        contentDescription = "Profile", modifier = Modifier.size(45.dp))
//                    Text(
//                        text = "Profile",
//                        modifier = Modifier
//                            .padding(horizontal = 25.dp),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }

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
        )

        MaterialTheme(
            typography = typography,
            content = content
        )
    }


    // HELPERS

    private fun fetchTouristDestinations(context: Context, onResult: (List<TouristDestination>) -> Unit) {

        apiHelper.getAllTourist(accessToken).enqueue(object : Callback<List<TouristDestination>> {
            override fun onResponse(
                call: Call<List<TouristDestination>>,
                response: Response<List<TouristDestination>>
            ) {
                Log.d("BerandaACt", "onResponse")
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
                    Log.d("BerandaACt", "IsSuccesful  ${response.body()}")
                }else
                {
                Log.d("BerandaACt", "IsNOtSuccesful  ${response.errorBody()?.string()}")


                 //dalam kasus unauth.. kedepannya perlu handling
                    val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<TouristDestination>>, t: Throwable) {
                // Handle error
                Log.d("BerandaACt", "onFailure")
            }
        })
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