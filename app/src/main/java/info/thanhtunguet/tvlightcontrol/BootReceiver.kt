package info.thanhtunguet.tvlightcontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            try {
                Log.d(TAG, "Boot completed, starting TVLightControl app")
                val startAppIntent = Intent(context, MainActivity::class.java)
                startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(startAppIntent)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting app on boot: ${e.message}")
            }
        }
    }
}
