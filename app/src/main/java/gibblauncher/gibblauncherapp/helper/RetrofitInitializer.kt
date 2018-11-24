package gibblauncher.gibblauncherapp.helper

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInitializer {

    var okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.105:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    fun apiService() = retrofit.create(APIService::class.java)
}
