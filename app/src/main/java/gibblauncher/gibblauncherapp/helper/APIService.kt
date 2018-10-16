package gibblauncher.gibblauncherapp.helper

import gibblauncher.gibblauncherapp.model.Test
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @GET("/")
    fun get() : Call<Test>

    @POST("/")
    fun post()
}
