package gibblauncher.gibblauncherapp.helper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://0.0.0.0:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun apiService() = retrofit.create(APIService::class.java)
}
