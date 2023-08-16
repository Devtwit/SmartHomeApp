package Reciever

import AwsConfigThing.AwsConfigClass
import Service.BleScanService
import Service.BleScanService.Companion.NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
import com.example.bthome.R

class NotificationButtonReceiver : BroadcastReceiver() {

    var awsConfig: AwsConfigClass? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(context!!)
        val action = intent?.action
        val updatedRemoteViews =
            RemoteViews(context?.packageName, R.layout.custom_notification_layout)
        updatedRemoteViews.setTextViewText(R.id.notificationTitle, "BLE Scanning Service")
        when (action) {

            ACTION_BUTTON_1 -> {
                Log.d("NotificationButtonReceiver", "Button1")
                updatedRemoteViews.setInt(R.id.button1, "setTextColor", Color.GRAY)
                updatedRemoteViews.setTextViewText(
                    R.id.notificationText,
                    "\n Light : Off \n Fan : ${AwsConfigClass.fan_status.capitalize()}"
                )
                var button_Status = AwsConfigClass.fan_status
                button_Status = if (button_Status.capitalize().equals("On")) "Off" else "On"


                updatedRemoteViews.setTextViewText(
                    R.id.button1,
                    "Light On"
                )
                updatedRemoteViews.setTextViewText(
                    R.id.button2,
                    "Fan $button_Status"
                )

                Handler().postDelayed({
                    awsConfig!!.publishDeviceNameLightOn("BT-Beacon_room1")
                }, DELAY_TIME_IN_MILLIS.toLong())

            }

            ACTION_BUTTON_2 -> {
                Log.d("NotificationButtonReceiver", "Button2")

                updatedRemoteViews.setInt(R.id.button1, "setTextColor", Color.GRAY)
                updatedRemoteViews.setTextViewText(
                    R.id.notificationText,
                    "\n Light : On \n Fan : ${AwsConfigClass.fan_status.capitalize()}"
                )
                var button_Status = AwsConfigClass.fan_status
                button_Status = if (button_Status.capitalize().equals("On")) "Off" else "On"


                updatedRemoteViews.setTextViewText(
                    R.id.button1,
                    "Light Off"
                )
                updatedRemoteViews.setTextViewText(
                    R.id.button2,
                    "Fan $button_Status"
                )
                Handler().postDelayed({
                    awsConfig!!.publishDeviceNameLightOff("BT-Beacon_room1")
                }, DELAY_TIME_IN_MILLIS.toLong())

            }

            ACTION_BUTTON_3 -> {
                Log.d("NotificationButtonReceiver", "Button3")
                updatedRemoteViews.setInt(R.id.button2, "setTextColor", Color.GRAY)
                updatedRemoteViews.setTextViewText(
                    R.id.notificationText,
                    "\n Light : ${AwsConfigClass.light_status.capitalize()} \n Fan : Off"
                )

                var button_Status = AwsConfigClass.light_status
                button_Status = if (button_Status.capitalize().equals("On")) "Off" else "On"
                updatedRemoteViews.setTextViewText(
                    R.id.button1,
                    "Light $button_Status"
                )

                updatedRemoteViews.setTextViewText(
                    R.id.button2,
                    "Fan On"
                )

                Handler().postDelayed({
                    awsConfig!!.publishDeviceNameFanOn("BT-Beacon_room1")
                }, DELAY_TIME_IN_MILLIS.toLong())

            }

            ACTION_BUTTON_4 -> {
                Log.d("NotificationButtonReceiver", "Button4")
                updatedRemoteViews.setInt(R.id.button2, "setTextColor", Color.GRAY)
                updatedRemoteViews.setTextViewText(
                    R.id.notificationText,
                    "\n Light : ${AwsConfigClass.light_status.capitalize()} \n Fan : On"
                )
                var button_Status = AwsConfigClass.light_status
                button_Status = if (button_Status.capitalize().equals("On")) "Off" else "On"
                updatedRemoteViews.setTextViewText(
                    R.id.button1,
                    "Light $button_Status"
                )
                updatedRemoteViews.setTextViewText(
                    R.id.button2,
                    "Fan Off"
                )

                Handler().postDelayed({
                    awsConfig!!.publishDeviceNameFanOff("BT-Beacon_room1")
                }, DELAY_TIME_IN_MILLIS.toLong())

            }

        }
        // Update the notification with the modified RemoteViews
        val notificationManager = NotificationManagerCompat.from(context!!)
        val notificationBuilder =
            NotificationCompat.Builder(context!!, BleScanService.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_bluetooth_searching_24)
                .setCustomContentView(updatedRemoteViews) // Set the custom layout
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setOngoing(true)
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setCustomContentView(updatedRemoteViews).build()
        )

    }

    companion object {
        const val ACTION_BUTTON_1 = "ACTION_BUTTON_1"
        const val ACTION_BUTTON_2 = "ACTION_BUTTON_2"
        const val ACTION_BUTTON_3 = "ACTION_BUTTON_3"
        const val ACTION_BUTTON_4 = "ACTION_BUTTON_4"
        const val DELAY_TIME_IN_MILLIS = 1000 // Adjust the delay time as needed (in milliseconds)
    }
}
