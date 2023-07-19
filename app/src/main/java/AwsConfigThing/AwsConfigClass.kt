package AwsConfigThing

import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
import DatabaseHelper
//import Database.DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.example.bthome.SmartActivity
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.receivedNearestDeviceName
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.responseAdapter
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
    var databaseHelper: DatabaseHelper? = null
    fun startAwsConfigurations(context: Context?) {
        databaseHelper = DatabaseHelper(context)
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
                    Log.d(TAG, "Status = $status")
                    when (status) {
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting -> {
                            Log.d(TAG, "Connecting...")
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected -> {
                            Log.d(TAG, "Connected")
                            subscribeToTopic(SET_CONFIG, context)
                            subscribeToTopic(GET_CONFIG, context)
                            subscribeToTopic("sdk/Falcon/setconfig_ack", context)
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

        // Handle the acknowledgment status
        val devices = responseData.devices
        val location = responseData.location

        val dbHelper = DatabaseHelper(context)
        dbHelper.insertData(location, devices)

        // Handle the acknowledgment status and error message as desired
        for ((deviceName, deviceData) in devices) {
            val status = deviceData["status"] as? String
            val ack = deviceData["ack"] as? String
            val errMsg = deviceData["err_msg"] as? String
            Log.d(TAG, "$deviceName ack : $ack")
            if (ack == "true") {
                // Acknowledgment received
                Log.d(TAG, "$deviceName: Acknowledgment received")
                Log.d(TAG, "$deviceName Status: $status")
            } else {
                // Acknowledgment not received, handle the error message
                Log.d(TAG, "$deviceName: Error - $errMsg")
            }
        }
    }


    fun publishData(msg: String?, topic: String?) {
        try {
            mqttManager!!.publishString(msg, topic, AWSIotMqttQos.QOS0)
        } catch (e: Exception) {
            Log.e(TAG, "Publish error.", e)
        }
    }

    fun publishDeviceName(deviceName: String) {
        val json = """
        {
            "location": "$deviceName",
            "devices": {
                "light": {
                    "status": "on/off"
                },
                "fan": {
                    "status": "on/off"
                }
            }
        }
    """.trimIndent()

        val topic = SET_CONFIG
        publishData(json, topic)
    }

    private fun parseResponseJson(json: String): ResponseData {
        val jsonObject = JSONObject(json)
        val location = jsonObject.getString("location")
        val devicesObject = jsonObject.getJSONObject("devices")
        val devices = mutableMapOf<String, Map<String, Any>>()
        Log.d(TAG,"RESP LOCATION $location")
        Log.d(TAG,"RESP devicesObject $devicesObject")
        val keysIterator = devicesObject.keys()
        while (keysIterator.hasNext()) {
            val deviceName = keysIterator.next() as String
            val deviceData = devicesObject.getJSONObject(deviceName).toMap()
            devices[deviceName] = deviceData
            Log.d(TAG,"RESP devicesObject $deviceName $deviceData")
        }
//        jsonData = json
        return ResponseData(location, devices, "add")
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

    companion object {
        val TAG = AwsConfigClass::class.java.simpleName
        lateinit var jsonData : String
    }
}
