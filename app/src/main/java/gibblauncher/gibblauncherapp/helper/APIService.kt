package gibblauncher.gibblauncherapp.helper

import gibblauncher.gibblauncherapp.model.Bounces
import gibblauncher.gibblauncherapp.model.TrainingDataApi
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @POST("/")
    fun post(@Body trainingDataApi: TrainingDataApi) : Call<String>

    @GET("/positions")
    fun get(@Query ("mac") mac: String,
            @Query ("id_trainingResult") id_trainingResult: Int)  : Call<Bounces>
}
