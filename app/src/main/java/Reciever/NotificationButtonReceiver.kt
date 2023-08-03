package Reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == ACTION_BUTTON_1) {
            // Handle Button 1 click
            // Perform your desired function here
            Log.d("NotificationButtonReceiver","Button1")
        } else if (action == ACTION_BUTTON_2) {
            // Handle Button 2 click
            // Perform your desired function here
            Log.d("NotificationButtonReceiver","Button2")

        }
    }

    companion object {
        const val ACTION_BUTTON_1 = "ACTION_BUTTON_1"
        const val ACTION_BUTTON_2 = "ACTION_BUTTON_2"
    }
}
