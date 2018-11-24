package gibblauncher.gibblauncherapp.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val intent = Intent(context, ExampleService::class.java)
            context.startService(intent)
        }
    }