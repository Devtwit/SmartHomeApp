package AwsConfigThing

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
import DatabaseHelper
import Reciever.NotificationButtonReceiver
import Service.BleScanService
import Service.BleScanService.Companion.NOTIFICATION_ID
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.example.bthome.R
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

/*
AwsConfigClass is to config the AWS with Cognito-pool-ID and to connect, subscribe and publish any MQTT message
*/
class AwsConfigClass() {
    var mqttManager: AWSIotMqttManager? = null
    var clientId = UUID.randomUUID().toString()
    var credentialsProvider: CognitoCachingCredentialsProvider? = null
    var databaseHelper: DatabaseHelper? = null

    fun startAwsConfigurations(context: Context) {
        credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            AwsConfigConstants.COGNITO_POOL_ID,
            AwsConfigConstants.MY_REGION
        )

        mqttManager = AWSIotMqttManager(clientId, AwsConfigConstants.CUSTOMER_SPECIFIC_ENDPOINT)
        mqttManager!!.setMaxAutoReconnectAttempts(4)
        mqttManager!!.setKeepAlive(10)

        try {
            mqttManager!!.connect(credentialsProvider, object : AWSIotMqttClientStatusCallback {
                override fun onStatusChanged(
                    status: AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus,
                    throwable: Throwable?
                ) {
                    when (status) {
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting -> {
                            Log.d(TAG, "Connecting...")
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected -> {
                            Log.d(TAG, "Connected")
                            // Subscribe to topics here

                            Log.d(TAG, "Connected")
                            subscribeToTopic(SET_CONFIG, context)
                            subscribeToTopic(GET_CONFIG, context)
                            subscribeToTopic("sdk/Falcon/setconfig_ack", context)
//                            subscribeToTopic("sdk/Falcon/getconfig_ack", context)
                            Log.d(TAG, "Subscribed on: $SET_CONFIG")
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting -> {
                            if (throwable != null) {
                                Log.d(TAG, "Connection error.", throwable)
                            }
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost -> {
                            if (throwable != null) {
                                Log.e(TAG, "Connection error.", throwable)
                            }
                            Log.d(TAG, "Disconnected")
                        }
                        else -> {
                            Log.d(TAG, "Disconnected")
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Connection error...", e)
        }
    }

    fun subscribeToTopic(topic: String?, context: Context?) {
        try {
            mqttManager!!.subscribeToTopic(
                topic, AWSIotMqttQos.QOS0,
                object : AWSIotMqttNewMessageCallback {
                    override fun onMessageArrived(topic: String?, data: ByteArray?) {
                        try {
                            val message = String(data!!, Charsets.UTF_8)
                            val jsonData = message.trim()

                            jsonDataLightFan = jsonData
                            Log.d(TAG, "Message arrived: Handle AKN")
                            Log.d(TAG, "   Topic tag: $topic")
                            Log.d(TAG, " Message json data: $jsonData")

                            if (topic.equals("sdk/Falcon/setconfig_ack", ignoreCase = true)) {
                                handleAcknowledgment(jsonData, context)
                            }
                        } catch (e: UnsupportedEncodingException) {
                            Log.e(TAG, "Message encoding error.", e)
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, "Subscription error.", e)
        }
    }
    private fun loadImageFromUrl(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    private fun handleAcknowledgment(json: String, context: Context?) {
        Log.d(TAG, "$json: Acknowledgment received")
        val responseData = parseResponseJson(json)
         cx= context!!
        // Handle the acknowledgment status
        val devices = responseData.devices
        val location = responseData.location

        val dbHelper = DatabaseHelper(context)
        dbHelper.insertData(location, devices)
//        dbHelper.insertDataWithImage(location,devices,loadImageFromUrl("C:\\Users\\deswami\\BtHome2\\app\\src\\main\\res\\drawable\\gbulb.webp")!!)

        val initialData = dbHelper.getAllResponseData()

        Log.d("Response dataUI"," ${initialData.toMutableList()}")
        var adapter  = RoomAdapter(initialData.toMutableList(), this)
        adapter.updateUI(initialData.toMutableList())
        adapter.notifyDataSetChanged()




        val notificationStringBuilder = StringBuilder()
        // Handle the acknowledgment status and error message as desired
        for ((deviceName, deviceData) in devices) {
            val status = deviceData["status"] as? String
            val ack = deviceData["ack"] as? String
            val errMsg = deviceData["err_msg"] as? String
            Log.d(TAG, "$deviceName ack : $ack")
            if (ack == "true") {
                // Acknowledgment received
                Log.d(TAG, "$deviceName: Acknowledgment received")
                if(deviceName.equals("light")){

                    light_status = status!!
                    Log.d(TAG, "$deviceName Status: $status light_status $light_status")
                } else {
                    Log.d(TAG, "$deviceName Status: $status")
                    fan_status = status!!
                    Log.d(TAG, "$deviceName Status: $status fan_status $fan_status")
                }
                notificationStringBuilder.append("\n $deviceName : $status")
                Log.d(TAG, "$deviceName Status: $status")
            } else {
                // Acknowledgment not received, handle the error message
                Log.d(TAG, "$deviceName: Error - $errMsg")
            }

            Log.d(TAG,"notificationContent ${notificationStringBuilder.toString()}")
           updateNotificationWithButtons(notificationStringBuilder.toString(), context)
        }

    }


    fun publishData(msg: String?, topic: String?) {
        try {
            Handler().postDelayed({
                mqttManager!!.publishString(msg, topic, AWSIotMqttQos.QOS0)
            }, 2000)
        } catch (e: Exception) {
            updateNotificationWithButtons("\n  Something went wrong, Please wait", cx)
            Log.e(TAG, "Publish error.", e)
        }
    }
    fun publishDeviceStatus(deviceName: String, lightStatus: String, fanStatus: String) {
        val json = """
        {
            "location": "$deviceName",
            "devices": {
                "light": {
                    "status": "$lightStatus"
                },
                "fan": {
                    "status": "$fanStatus"
                }
            }
        }
    """.trimIndent()

        val topic = SET_CONFIG
        publishData(json, topic)
    }

    fun publishDeviceTurnOnLight(deviceName: String) {
        publishDeviceStatus(deviceName, "on", "off")
    }

    fun publishDeviceTurnOnFan(deviceName: String) {
        publishDeviceStatus(deviceName, "off", "on")
    }

    fun publishDeviceName(deviceName: String) {
        publishDeviceStatus(deviceName, "on/off", "on/off")
    }

    fun publishDeviceNameOff(deviceName: String) {
        publishDeviceStatus(deviceName, "off", "off")
    }

    fun publishDeviceNameLightOff(deviceName: String) {
        publishDeviceStatus(deviceName, "off", fan_status)
    }

    fun publishDeviceNameLightOn(deviceName: String) {
        publishDeviceStatus(deviceName, "on", fan_status)
    }

    fun publishDeviceNameFanOn(deviceName: String) {
        publishDeviceStatus(deviceName, light_status, "on")
    }

    fun publishDeviceNameFanOff(deviceName: String) {
        publishDeviceStatus(deviceName, light_status, "off")
    }


    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keysIterator = keys()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next() as String
            val value = get(key)
            map[key] = value
        }
        return map
    }


    fun parseResponseJson(json: String): ResponseData {
        return try {
            val jsonObject = JSONObject(json)
            val location = jsonObject.getString("location")
            val devicesJson = jsonObject.getJSONObject("devices")

            val devices = mutableMapOf<String, Map<String, Any>>()

            val keys = devicesJson.keys()
            while (keys.hasNext()) {
                val deviceName = keys.next()
                val deviceData = devicesJson.getJSONObject(deviceName)
                val status = deviceData.getString("status")
                val ack = deviceData.getString("ack")
                val errMsg = deviceData.getString("err_msg")

                val deviceMap = mapOf(
                    "status" to status,
                    "ack" to ack,
                    "err_msg" to errMsg
                )

                devices[deviceName] = deviceMap
            }

            ResponseData(location, devices,location)
        } catch (e: JSONException) {
            // Handle the JSON parsing error here
            e.printStackTrace()
            // Return a default or error ResponseData object
            ResponseData("", emptyMap(),"Jsonerror $e")
        }
    }


    fun updateNotificationWithButtons(notificationContent: String, context: Context?) {
        val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val capString = capitalizeWords(notificationContent)
        Log.d(TAG,"notificationContent $capString")

        // Create a custom notification layout
        val contentView = RemoteViews(context!!.packageName, R.layout.custom_notification_layout)
        contentView.setTextViewText(R.id.notificationTitle, "BLE Scanning Service")
        contentView.setTextViewText(R.id.notificationText, capString)

        // Create a PendingIntent for the button clicks
        val actionIntent1 = Intent(context, NotificationButtonReceiver::class.java)
        val actionIntent2 = Intent(context, NotificationButtonReceiver::class.java)
        val actionIntent3 = Intent(context, NotificationButtonReceiver::class.java)
        val actionIntent4 = Intent(context, NotificationButtonReceiver::class.java)
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
        contentView.setTextColor(R.id.button1, Color.BLACK)
        contentView.setTextViewText(
            R.id.button2,
            if (fanStatus) "Fan Off" else "Fan On"
        )
        contentView.setTextColor(R.id.button2, Color.BLACK)

        val dStatus = capString
        val (light_Status, fan_Status) = parseDeviceStatus(dStatus)


        println("Light is ${if (light_Status) "on" else "off"}")
        println("Fan is ${if (fan_Status) "on" else "off"}")
        if(light_Status){
            Log.d(TAG,"Light is on")
//            contentView.setTextViewText(R.id.button1,"Light_off")
            val pendingIntent2 = PendingIntent.getBroadcast(context, 0, actionIntent2, PendingIntent.FLAG_IMMUTABLE)
            contentView.setOnClickPendingIntent(R.id.button1, pendingIntent2)
            contentView.setTextColor(R.id.button1, Color.BLACK)

        } else {
            Log.d(TAG,"Light is off")
//            contentView.setTextViewText(R.id.button1,"Light_on")
            val pendingIntent1 = PendingIntent.getBroadcast(context, 0, actionIntent1, PendingIntent.FLAG_IMMUTABLE)
            contentView.setOnClickPendingIntent(R.id.button1, pendingIntent1)
            contentView.setTextColor(R.id.button1, Color.BLACK)

        }
        if(fan_Status){
            Log.d(TAG,"Fan is on")
//            contentView.setTextViewText(R.id.button2,"Fan_off")
            val pendingIntent4 = PendingIntent.getBroadcast(context, 0, actionIntent4, PendingIntent.FLAG_IMMUTABLE)
            contentView.setOnClickPendingIntent(R.id.button2, pendingIntent4)
            contentView.setTextColor(R.id.button2, Color.BLACK)
        } else {
//            contentView.setTextViewText(R.id.button2,"Fan_on")
            val pendingIntent3 = PendingIntent.getBroadcast(context, 0, actionIntent3, PendingIntent.FLAG_IMMUTABLE)
            contentView.setOnClickPendingIntent(R.id.button2, pendingIntent3)
            contentView.setTextColor(R.id.button2, Color.BLACK)
            Log.d(TAG,"Fan is off")
        }
        // Create the notification
        val notification = NotificationCompat.Builder(context!!, BleScanService.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_bluetooth_searching_24)
            .setCustomContentView(contentView) // Set the custom layout
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
//        startForeground(NOTIFICATION_ID, notification)
    }

    fun parseDeviceStatus(deviceStatus: String): Pair<Boolean, Boolean> {
        val lines = deviceStatus.split("\n")
        var lStatus = false
        var fStatus = false

        for (line in lines) {
            val parts = line.split(": ")
            if (parts.size == 2) {
                val deviceName = parts[0].trim()
                val status = parts[1].trim()

                if (deviceName.equals("light", ignoreCase = true)) {
                    lStatus = status.equals("On", ignoreCase = true)
                } else if (deviceName.equals("fan", ignoreCase = true)) {
                    fStatus = status.equals("On", ignoreCase = true)
                }
            }
        }

        return Pair(lStatus, fStatus)
    }

    fun capitalizeWords(input: String): String {
        val words = input.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }


    companion object {
        val TAG = AwsConfigClass::class.java.simpleName
        var jsonDataLightFan : String = ""
        lateinit var jsonData : String
         var light_status : String = ""
       var fan_status : String = ""
        lateinit var cx:Context
        var isFromAws :Boolean = false
    }
}
