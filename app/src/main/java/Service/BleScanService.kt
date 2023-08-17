package Service

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants

import AwsConfigThing.AwsConfigConstants.Companion.UUID_FILTER
import Bluetooth.LeScanCallback

import Reciever.NotificationButtonReceiver
import android.annotation.SuppressLint
import android.app.*
import android.app.job.JobParameters
import android.app.job.JobService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.bthome.CustomNotificationHandler
import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment
import java.util.*
import kotlin.collections.ArrayList

class BleScanService : JobService() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private val parcelUuid: ParcelUuid = ParcelUuid(UUID.fromString(UUID_FILTER))
    private val scanFilters: MutableList<ScanFilter> = ArrayList()
    private val scanSettings: ScanSettings =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
    var awsConfig: AwsConfigClass? = null

    companion object {
        //        private const val UUID_FILTER = "Your_UUID_Filter" // Replace with your specific UUID
        const val NOTIFICATION_CHANNEL_ID = "BleScanServiceChannel"
        private const val TAG = "BleScanService"

        const val NOTIFICATION_ID = 1
        const val ALARM_INTERVAL_MILLIS =
            5 * 60 * 1000 // Set the interval in milliseconds (5 minutes in this example)

    }

    private val alarmHandler = Handler()
    private val alarmRunnable = Runnable {
        // Restart the foreground service periodically after the specified interval
        startForegroundService()
    }

    // Use lateinit var for wakeLock
    private lateinit var wakeLock: PowerManager.WakeLock

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(applicationContext)
        val scanFilter = ScanFilter.Builder().setServiceUuid(parcelUuid).build()
        scanFilters.add(scanFilter)

        // Initialize wakeLock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BleScanService::WakeLock")

        val filter = IntentFilter().apply {
            addAction(NotificationButtonReceiver.ACTION_BUTTON_1)
            addAction(NotificationButtonReceiver.ACTION_BUTTON_2)
        }
        registerReceiver(NotificationButtonReceiver(), filter)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Acquire the wake lock
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(applicationContext)
        startForegroundService()
        awsConfig!!.subscribeToTopic(AwsConfigConstants.SET_CONFIG, applicationContext)
        awsConfig!!.subscribeToTopic(AwsConfigConstants.GET_CONFIG, applicationContext)
        awsConfig!!.subscribeToTopic("sdk/Falcon/setconfig_ack", applicationContext)

        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
//        stopForegroundService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        // Start the foreground service and show a notification to keep it running
        startForeground(NOTIFICATION_ID, createNotification())

        // Start BLE scanning here
        bluetoothLeScanner.startScan(scanFilters, scanSettings, leScanCallback)

        // Schedule the alarm to trigger the service after the specified interval
        alarmHandler.postDelayed(alarmRunnable, ALARM_INTERVAL_MILLIS.toLong())
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        startBleScanning()
        return true // Return true if the job needs to continue running even if onStartJob() returns.
    }

    override fun onStopJob(params: JobParameters?): Boolean {
//        stopBleScanning()
        return true // Return true to reschedule the job if it needs to be rescheduled.
    }

    private fun startBleScanning() {
        bluetoothLeScanner.startScan(scanFilters, scanSettings, leScanCallback)
    }

    private fun stopBleScanning() {
        bluetoothLeScanner.stopScan(leScanCallback)
    }

    private fun stopForegroundService() {
        // Stop BLE scanning here
        bluetoothLeScanner.stopScan(leScanCallback)

        // Stop the foreground service and remove the notification
        stopForeground(true)
        stopSelf()

        // Cancel the scheduled alarm
        alarmHandler.removeCallbacks(alarmRunnable)
    }

    // Create an instance of LeScanCallback
    private val leScanCallback = LeScanCallback(object : LeScanCallback.DeviceFound {
        override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
            // Process the scan results here
            // You can send broadcast, update UI, etc.
            if (mScanResult!!.isEmpty()) {
                Log.d(
                    TAG,
                    "AWS CONFIG NO DEVICE FOUND"
                )
//                awsConfig!!.publishDeviceName("No device found")
                return
            }

            Log.d("BleScanService", "Scan result +$result")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(
                    TAG,
                    "Device " + result!!.device.address + " rssi: " + result.rssi + " Tx: " + result.txPower
                )
                if (result.rssi <= -20 && result.rssi > -70) {
                    Log.d(
                        TAG,
                        "Device " + result.device.name + "address :" + result.device.address + " rssi: " + result.rssi + " Onside if"
                    )
                    if (result.device.name != null) {
//                     AddBleDeviceFragment().awsConfig!!.publishDeviceName("BT-Beacon_room1")
//                        awsConfig!!.publishDeviceName("BT-Beacon_room1")
                        AddBleDeviceFragment.receivedNearestDeviceName = result.device.name

                    } else {

                        AddBleDeviceFragment.receivedNearestDeviceName = "BT-Beacon_room1"
                    }
                    Log.d(
                        TAG,
                        "AWS CONFIG AWS CONFIG DEVICE FOUND"
                    )

                } else {
                    Log.d(
                        TAG,
                        "AWS CONFIG NO DEVICE FOUND"
                    )
//                    awsConfig!!.publishDeviceName("No device found")
                    AddBleDeviceFragment.receivedNearestDeviceName = ""
                }

            }
            val deviceName = result?.device?.name ?: ""
            handleAcknowledgment(AwsConfigClass.jsonDataLightFan, applicationContext)
            Log.d(TAG, "Inside if call back ")

        }
    })

    private fun handleAcknowledgment(deviceName: String, context: Context?) {
//        val jsonDataString =

        val jsonData = awsConfig!!.parseResponseJson(deviceName)
        Log.d(TAG, "Inside if handleAcknowledgment $jsonData")
        val devices = jsonData.devices
        val notificationStringBuilder = StringBuilder()
        for ((deviceName, deviceData) in devices) {
            val status = deviceData["status"] as? String
            val ack = deviceData["ack"] as? String
            val errMsg = deviceData["err_msg"] as? String

            if (ack == "true") {
                Log.d(TAG, "Inside if handleAcknowledgment ")
                if (deviceName.equals("light")) {

                    AwsConfigClass.light_status = status!!
                    Log.d(
                        AwsConfigClass.TAG,
                        "$deviceName Status: $status light_status ${AwsConfigClass.light_status}"
                    )
                } else {
                    Log.d(AwsConfigClass.TAG, "$deviceName Status: $status")
                    AwsConfigClass.fan_status = status!!
                    Log.d(
                        AwsConfigClass.TAG,
                        "$deviceName Status: $status fan_status ${AwsConfigClass.fan_status}"
                    )
                }
                // Append device information to the notification string
                notificationStringBuilder.append("\n $deviceName : $status")

                // Acknowledgment received, update notification with device status
                Log.d(TAG, "$deviceName: Acknowledgment received")
                Log.d(TAG, "$deviceName Status: $status")
            } else {
                // Acknowledgment not received, handle the error message
                notificationStringBuilder.append("$deviceName: Error - $errMsg\n")
                Log.d(TAG, "Inside else handleAcknowledgment ")
                Log.d(TAG, "$deviceName: Error - $errMsg")
            }
            // Update the notification with the accumulated information
            CustomNotificationHandler.updateNotificationWithButtons(
                notificationStringBuilder.toString(),
                context
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "BLE Scan Service Channel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
        notificationManager.createNotificationChannel(channel)
        // Create a PendingIntent for the button clicks
        val actionIntent1 = Intent(applicationContext, NotificationButtonReceiver::class.java)
        actionIntent1.action = "ACTION_BUTTON_1"
        val pendingIntent1 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent1,
            PendingIntent.FLAG_IMMUTABLE
        )

        val actionIntent2 = Intent(applicationContext, NotificationButtonReceiver::class.java)
        actionIntent2.action = "ACTION_BUTTON_2"
        val pendingIntent2 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent2,
            PendingIntent.FLAG_IMMUTABLE
        )

        val actionIntent3 = Intent(applicationContext, NotificationButtonReceiver::class.java)
        val actionIntent4 = Intent(applicationContext, NotificationButtonReceiver::class.java)

        actionIntent3.action = "ACTION_BUTTON_3"
        actionIntent4.action = "ACTION_BUTTON_4"
        val pendingIntent3 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent3,
            PendingIntent.FLAG_IMMUTABLE
        )

        val pendingIntent4 = PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionIntent4,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Create a custom notification layout with RemoteViews
        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification_layout)
        notificationLayout.setTextViewText(R.id.notificationTitle, "BLE Scanning Service")
        notificationLayout.setTextViewText(R.id.notificationText, "Scanning for BLE devices")
        notificationLayout.setOnClickPendingIntent(R.id.button1, pendingIntent1)
        notificationLayout.setOnClickPendingIntent(R.id.button1, pendingIntent2)
        notificationLayout.setOnClickPendingIntent(R.id.button2, pendingIntent3)
        notificationLayout.setOnClickPendingIntent(R.id.button2, pendingIntent4)

        // Create the notification
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_bluetooth_searching_24)
            .setCustomContentView(notificationLayout)
            .setOngoing(true)
            .build()
    }


}
