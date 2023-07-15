package AwsConfigThing

import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
import Database.DatabaseHelper
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
import java.io.UnsupportedEncodingException
import java.util.UUID

/*
AwsConfigClass is to config the AWS with Cognito-pool-ID and to connect, subscribe and publish any mqtt message
* */
class AwsConfigClass() {
    var mqttManager: AWSIotMqttManager? = null
    var clientId = UUID.randomUUID().toString()
    var credentialsProvider: CognitoCachingCredentialsProvider? = null
    fun startAwsConfigurations(context: Context?) {
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
                override fun onStatusChanged(status: AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus, throwable: Throwable?) {
                    Log.d(TAG, "Status = $status")
                    Log.d(TAG, "Status = $status")
                    when(status){
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting ->{
                            Log.d(TAG, "Connecting...")
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected ->{
                            Log.d(TAG, "Connected")
                            subscribeToTopic(SET_CONFIG,context)
                            subscribeToTopic(GET_CONFIG,context)
                            Log.d(TAG, "Subscribed on : $SET_CONFIG")
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting->{
                            if (throwable != null) {
                                Log.d(TAG, "Connection error.", throwable)
                            }
                        }
                        AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost->{
                            if (throwable != null) {
                                Log.e(TAG, "Connection error.", throwable)
                            }
                            Log.d(TAG, "Disconnected")
                        }
                        else -> { Log.d(TAG, "Disconnected")}
                    }
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Connection error...", e)
        }
    }

    fun subscribeToTopic(topic: String?,context: Context?) {
        try {
            mqttManager!!.subscribeToTopic(topic, AWSIotMqttQos.QOS0,
                object : AWSIotMqttNewMessageCallback {
                    override fun onMessageArrived(topic: String, data: ByteArray?) {
                        try {
                            val message = String(data!!, Charsets.UTF_8)
                            Log.d(TAG, "Message arrived:")
                            Log.d(TAG, "   Topic: $topic")
                            Log.d(TAG, " Message: $message")
                           receivedNearestDeviceName = message
                            if (topic.equals(SET_CONFIG, ignoreCase = true)) {
                                receivedNearestDeviceName = message
                            }
                          var s = message.split(" ")

                            // Store the response in the database
                            val responseData = ResponseData(topic, s[0],s[1])
                            storeResponseInDatabase(responseData, context )
                        } catch (e: UnsupportedEncodingException) {
                            Log.e(TAG, "Message encoding error.", e)
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, "Subscription error.", e)
        }
    }

    private fun storeResponseInDatabase(responseData: ResponseData,context: Context?) {
        // Initialize your SQLiteOpenHelper or database client
        val dbHelper = DatabaseHelper(context!! ) // Replace with your actual database helper class

        // Get a writable database instance
        val db = dbHelper.writableDatabase

       if(!responseData.message.equals("No device found in range")) {
           // Create a ContentValues object to store the data
           val values = ContentValues().apply {
               put("topic", responseData.topic)
               put("message", responseData.message)
               put("address", responseData.address)
           }

           // Insert the data into the appropriate table
           db.insert("response_table", null, values)
           // Insert the data into the appropriate table
           val newRowId = db.insert("response_table", null, values)

           // Fetch the updated data from the database
           val updatedData = dbHelper.getAllResponseData()
           // Initialize the adapter

           // Update the adapter with the new data
           (context as SmartActivity ).runOnUiThread {
               responseAdapter.updateData(updatedData)
           }
       } else {
           receivedNearestDeviceName = ""
       }

        // Close the database connection
        db.close()
    }


    /*
    publishData is to publish mqtt data to a topic
    * */
    fun publishData(msg: String?, topic: String?) {
        try {
            mqttManager!!.publishString(msg, topic, AWSIotMqttQos.QOS0)
        } catch (e: Exception) {
            Log.e(TAG, "Publish error.", e)
        }
    }

    companion object {
        val TAG = AwsConfigClass::class.java.simpleName
    }
}