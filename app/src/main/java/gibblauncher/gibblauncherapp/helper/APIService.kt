package gibblauncher.gibblauncherapp.helper

import gibblauncher.gibblauncherapp.model.Bounces
import retrofit2.Call
import retrofit2.http.POST

interface APIService {
    @POST("/")
    fun post() : Call<Bounces>
}
