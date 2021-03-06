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
import android.util.Log
import java.net.NetworkInterface
import java.util.*
import android.widget.Toast
import gibblauncher.gibblauncherapp.helper.NotificationService


class SplashScreenActivity : AppCompatActivity() {

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
        return currentConnectedSSID == WifiConnectActivity.GIBBlAUNCHER_NETWORK && currentConnetedBSSID != null
    }

    private fun changeActivity (){
        val wifiManager =  applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        val isConnectedGibbNetwork = checkIsConnetedGibbNetwork(wifiInfo)

        var intent : Intent

        if(isConnectedGibbNetwork){
            intent = Intent(this, MainActivity::class.java)
            val ip = getIP(wifiManager)
            val mac = getMAC(wifiManager)
            intent.putExtra("IP", ip)
            intent.putExtra("MAC", mac)

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

    private fun getMAC(wifiManager: WifiManager) : String {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.getName().equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.getHardwareAddress() ?: return ""

                Log.d("MAC", macBytes.toString())


                val res1 = StringBuilder()
                for (b in macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b))
                }
                Log.d("MAC2 ", res1.toString())


                if (res1.length > 0) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
        }

        return "02:00:00:00:00:00"
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
