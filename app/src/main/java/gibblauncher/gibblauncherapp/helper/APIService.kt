package gibblauncher.gibblauncherapp.helper

import gibblauncher.gibblauncherapp.model.Bounces
import gibblauncher.gibblauncherapp.model.TrainingDataApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("/")
    fun post(@Body trainingDataApi: TrainingDataApi) : Call<Bounces>
}
