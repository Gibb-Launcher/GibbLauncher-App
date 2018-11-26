package gibblauncher.gibblauncherapp.helper

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.controller.MainActivity
import gibblauncher.gibblauncherapp.controller.SplashScreenActivity
import gibblauncher.gibblauncherapp.model.BounceLocation
import gibblauncher.gibblauncherapp.model.Bounces
import gibblauncher.gibblauncherapp.model.TrainingResult
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_aleatory_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket

class NotificationService : IntentService("NotificationService") {
    var server : ServerSocket? = null
    var cont: Int = 1
    var notificationResponse: List<String>? = null

    var mMainThreadHandler: Handler? = null

    override fun onCreate() {
        super.onCreate()
        mMainThreadHandler = Handler()

    }

    override fun onHandleIntent(workIntent: Intent) {

        try {
            server = ServerSocket(4444)

            while (true) {

                val socket = server?.accept()
                val rec = BufferedReader(
                        InputStreamReader(socket?.getInputStream()))

                val str = rec.readLine()
                notificationResponse = str.split(';')
                //showToast(str)
                showNotification(notificationResponse!![0])
                rec.close()
                socket?.close()
            }
        } catch (e: IOException) {
            Log.e("IOException", "")
        } catch (e: Exception) {
            Log.e("Exception", "")
        }
    }

    fun showToast(msg: String) {
        mMainThreadHandler?.post(Runnable {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        })
    }

    fun showNotification(msg: String){
            mMainThreadHandler?.post(Runnable {
                val notificationIntent = Intent(applicationContext, MainActivity::class.java)
                val contentIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)

                var notificationBuilder = NotificationCompat.Builder(applicationContext, "Canal")
                        .setContentTitle("Resultado do Treino")
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.glogo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.logol))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(longArrayOf(500, 1000))
                        .setContentIntent(contentIntent)
                        .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel("Canal",
                            "Titulo do Canal",
                            NotificationManager.IMPORTANCE_DEFAULT)
                    notificationManager.createNotificationChannel(channel)
                }
                notificationManager.notify(cont, notificationBuilder.build())
                cont++
            })

        var id_trainingResult = notificationResponse!![2].toInt()
        var mac = notificationResponse!![1]

        val call = RetrofitInitializer().apiService().get(mac, id_trainingResult)
        call!!.enqueue(object: Callback<Bounces> {
            override fun onResponse(call: Call<Bounces>?,
                                    response: Response<Bounces>?) {

                response?.body()?.let {
                    saveBounceLocationInDatabase(it.bounces, id_trainingResult)
                }
            }

            override fun onFailure(call: Call<Bounces>?,
                                   t: Throwable?) {
                Log.d("Notification Service", "Erro no get")
            }
        })

    }

    private fun saveBounceLocationInDatabase(bounces : List<BounceLocation>, id_trainingResult : Int) {
            // Open the realm for the UI thread.
        var realm = Realm.getDefaultInstance()

            try {
                realm.executeTransaction { realm ->
                    // Add a Training
                    var trainingResult = realm.where<TrainingResult>().equalTo("id", id_trainingResult).findFirst()

                    for(bounce in bounces){
                        if (trainingResult != null) {
                            trainingResult.bouncesLocations.add(bounce)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Erro", e.message)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (server != null) {
            try {
                server?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


}

