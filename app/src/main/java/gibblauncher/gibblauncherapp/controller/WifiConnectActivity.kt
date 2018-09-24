package gibblauncher.gibblauncherapp.controller


import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

import com.google.zxing.integration.android.IntentIntegrator
import gibblauncher.gibblauncherapp.R

class WifiConnectActivity : AppCompatActivity() {

        private var wc: WifiConfiguration = WifiConfiguration()
        private var wifi: WifiManager? = null
        private var key: String? = null
        private var networkName: String? = null


    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wificonnect)
        wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        var btQrcode: Button = findViewById(R.id.bt_QRCode)

        btQrcode.setOnClickListener { openQRCode() }

    }

    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?) {
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null){
            if(result.contents != null){
                getNetworkInformation(result.contents)
                configNetworkWifi()
                connectNetwork()
            } else {
                Toast.makeText(applicationContext, getString(R.string.cancelled_scan), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


    fun desconnectNetwork() {
        var info: WifiInfo = wifi!!.connectionInfo
        var id: Int = info.networkId
        wifi!!.removeNetwork(id)
    }

    fun configNetworkWifi() {
        wifi!!.setWifiEnabled(true)
        wc.SSID = networkName
        wc.preSharedKey = String.format("\"%s\"", key)
        wc.hiddenSSID = true
        wc.status = WifiConfiguration.Status.ENABLED
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
    }

    fun connectNetwork() {
        val result: Int = wifi!!.addNetwork(wc)
        var isConnect: Boolean = wifi!!.enableNetwork(result, true)
        if (isConnect) {
            //TODO call next activity"
        } else {
            Toast.makeText(applicationContext, (getString(R.string.network_not_found)), Toast.LENGTH_LONG).show()
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
        networkName = configurationNetwork[0]
        key = configurationNetwork[1]
    }
}
