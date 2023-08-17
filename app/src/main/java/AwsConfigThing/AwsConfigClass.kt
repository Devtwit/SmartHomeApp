package AwsConfigThing

import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
import DatabaseHelper
import android.content.Context
import android.os.Handler
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.example.bthome.CustomNotificationHandler.Companion.updateNotificationWithButtons
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.UUID

/*
AwsConfigClass is to config the AWS with Cognito-pool-ID and to connect, subscribe and publish any MQTT message
*/
class AwsConfigClass() {
    var mqttManager: AWSIotMqttManager? = null
    var clientId = UUID.randomUUID().toString()
    var credentialsProvider: CognitoCachingCredentialsProvider? = null


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

    private fun handleAcknowledgment(json: String, context: Context?) {
        Log.d(TAG, "$json: Acknowledgment received")
        val responseData = parseResponseJson(json)
         cx= context!!
        // Handle the acknowledgment status
        val devices = responseData.devices
        val location = responseData.location

        val dbHelper = DatabaseHelper(context)
        dbHelper.insertData(location, devices)
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


    companion object {
        val TAG = AwsConfigClass::class.java.simpleName
        var jsonDataLightFan : String = ""
        lateinit var jsonData : String
         var light_status : String = ""
       var fan_status : String = ""
        lateinit var cx:Context
    }
}
