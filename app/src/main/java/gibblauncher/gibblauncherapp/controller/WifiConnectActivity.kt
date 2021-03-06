package gibblauncher.gibblauncherapp.controller


import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.helper.NotificationService
import java.lang.Thread.sleep
import java.net.NetworkInterface
import java.util.*


class WifiConnectActivity : AppCompatActivity() {
        val TAG:String="WifiActivity";
        companion object {
            val GIBBlAUNCHER_NETWORK : String = "GibbLauncher"
        }
        private var wc: WifiConfiguration = WifiConfiguration()
        private var wifi: WifiManager? = null
        private lateinit var key: String
        private lateinit var networkName: String


    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wificonnect)

        wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifi!!.isWifiEnabled = false
        wifi!!.isWifiEnabled = true

        var btQrcode: Button = findViewById(R.id.bt_QRCode)

        btQrcode.setOnClickListener { openQRCode() }

        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }




    private fun connectToWPAWiFi(ssid:String, pass:String){
        val wm:WifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.isWifiEnabled = true
        var wifiConfig=getWiFiConfig(ssid)
        if(wifiConfig==null){//if the given ssid is not present in the WiFiConfig, create a config for it
            createWPAProfile(ssid,pass)
            wifiConfig=getWiFiConfig(ssid)
        }
        wm.disconnect()
        wm.enableNetwork(wifiConfig!!.networkId,true)
        wm.reconnect()
        Toast.makeText(applicationContext, R.string.device_connected, Toast.LENGTH_SHORT).show()
        sleep(800)
        changeActivity(wm)
        Log.d(TAG, "intiated connection to SSID$ssid")
    }

    private fun getWiFiConfig(ssid: String): WifiConfiguration? {
        val wm:WifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.isWifiEnabled = true
        val wifiList=wm.configuredNetworks
        for (item in wifiList){
            if(item.SSID != null && item.SSID.equals(ssid)){
                return item
            }
        }
        return null
    }

    private fun createWPAProfile(ssid: String, pass: String){
        Log.d(TAG, "Saving SSID :$ssid")
        val conf = WifiConfiguration()
        conf.SSID = ssid
        conf.preSharedKey = pass
        val wm:WifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.addNetwork(conf)
        Log.d(TAG,"saved SSID to WiFiManger")
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?) {

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null){
            if(result.contents != null){
                getNetworkInformation(result.contents)
                connectToWPAWiFi(networkName, key)
            } else {
                Toast.makeText(applicationContext, getString(R.string.cancelled_scan), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun openQRCode() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setPrompt(getString(R.string.message_scanner))
        integrator.setCameraId(0)
        integrator.initiateScan()
    }

    private fun getNetworkInformation(QRCodeString: String ) {
        val configurationNetwork = QRCodeString.split(";")
        networkName = String.format("\"%s\"", configurationNetwork[0])
        key = String.format("\"%s\"", configurationNetwork[1])
    }

    private fun checkIsConnetedGibbNetwork(wifiInfo : WifiInfo): Boolean{

        val currentConnectedSSID = wifiInfo.ssid.replace("\"", "")
        val currentConnetedBSSID = wifiInfo.bssid
        return currentConnectedSSID == GIBBlAUNCHER_NETWORK && currentConnetedBSSID != null
    }

    private fun changeActivity (wifiManager : WifiManager){
        val wifiInfo = wifiManager.connectionInfo
        val isConnectedGibbNetwork = checkIsConnetedGibbNetwork(wifiInfo)

        var intent : Intent

        if(isConnectedGibbNetwork){
            intent = Intent(this, MainActivity::class.java)
            val ip = getIP(wifiManager)
            val mac = getMAC(wifiManager)
            intent.putExtra("IP", ip)
            intent.putExtra("MAC", mac)
            finish()
            startActivity(intent)

        } else{
            Toast.makeText(applicationContext, "O dispositivo não está na rede do GibbLauncher", Toast.LENGTH_LONG).show()
        }

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

    override fun onPause() {
        super.onPause()
    }
}
