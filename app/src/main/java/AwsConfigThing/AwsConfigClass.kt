package AwsConfigThing

import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment
import java.io.UnsupportedEncodingException
import java.util.UUID

/*
AwsConfigClass is to config the AWS with Cognito-pool-ID and to connect, subscribe and publish any mqtt message
* */
class AwsConfigClass {
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
                            subscribeToTopic(SET_CONFIG)
                            subscribeToTopic(GET_CONFIG)
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

    /*
    subscribeToTopic is to subscribe to any mqtt topic
    * */
    fun subscribeToTopic(topic: String?) {
        try {
            mqttManager!!.subscribeToTopic(topic, AWSIotMqttQos.QOS0,
                object : AWSIotMqttNewMessageCallback {
                    override fun onMessageArrived(topic: String, data: ByteArray?) {
                        try {
                            val message = String(data!!, Charsets.UTF_8)
                            Log.d(TAG, "Message arrived:")
                            Log.d(TAG, "   Topic: $topic")
                            Log.d(TAG, " Message: $message")
                            if (topic.equals(SET_CONFIG, ignoreCase = true)) {
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