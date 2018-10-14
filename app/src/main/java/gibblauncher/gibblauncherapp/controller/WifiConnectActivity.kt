package gibblauncher.gibblauncherapp.controller


import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import gibblauncher.gibblauncherapp.R

class WifiConnectActivity : AppCompatActivity() {
        val TAG:String="WifiActivity";
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
        changeActivity()
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

    private fun changeActivity(){
        var intent : Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}
