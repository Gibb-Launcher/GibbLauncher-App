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
import android.support.v4.content.ContextCompat



class SplashScreenActivity : AppCompatActivity() {

    private val GIBBlAUNCHER_NETWORK : String = "GVT-F243"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        checkPermition()

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

        } else{
            intent = Intent(this, WifiConnectActivity::class.java)
        }
        finish()
        startActivity(intent)
    }
}