import com.example.tourismo.api.ApiEndpoint
import com.example.tourismo.api.HeaderInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://tourismobe-mugv7adrmq-uc.a.run.app/api/"


    val gson = GsonBuilder()
        .setLenient()
        .create()

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
//        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiInstance = retrofit.create(ApiEndpoint::class.java)
}