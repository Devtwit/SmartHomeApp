package com.example.bthome

import Reciever.NotificationButtonReceiver
import Service.BleScanService
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.widget.RemoteViews

class CustomNotificationHandler {
    companion object{

        val TAG ="CustomNotificationHandler"
        fun capitalizeWords(input: String): String {
            val words = input.split(" ")
            val capitalizedWords = words.map { it.capitalize() }
            return capitalizedWords.joinToString(" ")
        }

        @SuppressLint("UnspecifiedRegisterReceiverFlag")
        fun updateNotificationWithButtons(notificationContent: String, context: Context?) {
            val notificationManager = context!!.getSystemService(JobService.NOTIFICATION_SERVICE) as NotificationManager
            val capString = capitalizeWords(notificationContent)

            Log.d(TAG,"notificationContent $capString")

            // Create a custom notification layout
            val contentView = RemoteViews(context!!.packageName, R.layout.custom_notification_layout)
            contentView.setTextViewText(R.id.notificationTitle, "BLE Scanning Service")
            contentView.setTextViewText(R.id.notificationText, capString)

            // Create a PendingIntent for the button clicks
            val actionIntent1 = Intent(context!!, NotificationButtonReceiver::class.java)
            val actionIntent2 = Intent(context!!, NotificationButtonReceiver::class.java)
            val actionIntent3 = Intent(context!!, NotificationButtonReceiver::class.java)
            val actionIntent4 = Intent(context!!, NotificationButtonReceiver::class.java)
            actionIntent1.action = "ACTION_BUTTON_1"
            actionIntent2.action = "ACTION_BUTTON_2"
            actionIntent3.action = "ACTION_BUTTON_3"
            actionIntent4.action = "ACTION_BUTTON_4"



            // Update the button text based on the device status
            val (lightStatus, fanStatus) = parseDeviceStatus(capString)
            contentView.setTextViewText(
                R.id.button1,
                if (lightStatus) "Light Off" else "Light On"
            )
            contentView.setTextViewText(
                R.id.button2,
                if (fanStatus) "Fan Off" else "Fan On"
            )
            contentView.setTextColor(R.id.button1, Color.BLACK)
            contentView.setTextColor(R.id.button2, Color.BLACK)

            val dStatus = notificationContent
            val (light_Status, fan_Status) = parseDeviceStatus(dStatus)

            println("Light is ${if (light_Status) "on" else "off"}")
            println("Fan is ${if (fan_Status) "on" else "off"}")
            if(light_Status){
                Log.d(TAG,"Light is on")
                val pendingIntent2 = PendingIntent.getBroadcast(context!!, 0, actionIntent2, PendingIntent.FLAG_IMMUTABLE)
                contentView.setOnClickPendingIntent(R.id.button1, pendingIntent2)
                contentView.setTextColor(R.id.button1, Color.BLACK)

            } else {
                Log.d(TAG,"Light is off")
                val pendingIntent1 = PendingIntent.getBroadcast(context!!, 0, actionIntent1, PendingIntent.FLAG_IMMUTABLE)
                contentView.setOnClickPendingIntent(R.id.button1, pendingIntent1)
                contentView.setTextColor(R.id.button1, Color.BLACK)

            }
            if(fan_Status){
                Log.d(TAG,"Fan is on")
                val pendingIntent4 = PendingIntent.getBroadcast(context!!, 0, actionIntent4, PendingIntent.FLAG_IMMUTABLE)
                contentView.setOnClickPendingIntent(R.id.button2, pendingIntent4)
                contentView.setTextColor(R.id.button2, Color.BLACK)
            } else {
                val pendingIntent3 = PendingIntent.getBroadcast(context!!, 0, actionIntent3, PendingIntent.FLAG_IMMUTABLE)
                contentView.setOnClickPendingIntent(R.id.button2, pendingIntent3)
                contentView.setTextColor(R.id.button2, Color.BLACK)
                Log.d(TAG,"Fan is off")
            }
            // Create the notification
            val notification = NotificationCompat.Builder(context!!,
                BleScanService.NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.baseline_bluetooth_searching_24)
                .setCustomContentView(contentView) // Set the custom layout
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setOngoing(true)
                .build()



            notificationManager.notify(BleScanService.NOTIFICATION_ID, notification)

//            Started fore ground service
            BleScanService().startForeground(BleScanService.NOTIFICATION_ID, notification)
        }
       private fun parseDeviceStatus(deviceStatus: String): Pair<Boolean, Boolean> {
            val lines = deviceStatus.split("\n")
            var lStatus = false
            var fStatus = false

            for (line in lines) {
                val parts = line.split(": ")
                if (parts.size == 2) {
                    val deviceName = parts[0].trim()
                    val status = parts[1].trim()

                    if (deviceName.equals("light", ignoreCase = true)) {
                        lStatus = status.equals("on", ignoreCase = true)
                    } else if (deviceName.equals("fan", ignoreCase = true)) {
                        fStatus = status.equals("on", ignoreCase = true)
                    }
                }
            }

            return Pair(lStatus, fStatus)
        }
    }

}