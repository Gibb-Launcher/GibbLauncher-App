package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import gibblauncher.gibblauncherapp.R
import android.net.wifi.WifiInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import java.net.ServerSocket
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import android.app.NotificationManager
import android.os.Handler
import gibblauncher.gibblauncherapp.helper.NotificationService


class SplashScreenActivity : AppCompatActivity() {

    private val GIBBlAUNCHER_NETWORK : String = "GibbLauncher"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        checkPermition()
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    changeActivity()

                } else {

                    checkPermition()
                }
                return
            }
        }
    }

    private fun checkPermition(){

        if (ContextCompat.checkSelfPermission(this@SplashScreenActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            var fineLocation = listOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION)

            ActivityCompat.requestPermissions(this@SplashScreenActivity, fineLocation.toTypedArray(),
                        1)

        } else {
            changeActivity()
        }
    }

    private fun checkIsConnetedGibbNetwork(wifiInfo : WifiInfo): Boolean{

        val currentConnectedSSID = wifiInfo.ssid.replace("\"", "")
        val currentConnetedBSSID = wifiInfo.bssid
        return currentConnectedSSID == GIBBlAUNCHER_NETWORK && currentConnetedBSSID != null
    }

    private fun changeActivity (){
        val wifiManager =  applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        val isConnectedGibbNetwork = checkIsConnetedGibbNetwork(wifiInfo)

        var intent : Intent

        if(isConnectedGibbNetwork){
            intent = Intent(this, MainActivity::class.java)
            val ip = getIP(wifiManager)
            intent.putExtra("IP", ip)

        } else{
            intent = Intent(this, WifiConnectActivity::class.java)
        }
        finish()
        startActivity(intent)
    }

    private fun getIP(wifiManager: WifiManager) : String {
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val ip = String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
        return ip
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
