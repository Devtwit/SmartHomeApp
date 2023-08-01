package Service

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.UUID_FILTER
import Bluetooth.LeScanCallback
import Data.ResponseData
import DatabaseHelper
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
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment
import java.util.*
import kotlin.collections.ArrayList

class BleScanService : JobService() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private val parcelUuid: ParcelUuid = ParcelUuid(UUID.fromString(UUID_FILTER))
    private val scanFilters: MutableList<ScanFilter> = ArrayList()
    private val scanSettings: ScanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
    var awsConfig: AwsConfigClass? = null
    companion object {
//        private const val UUID_FILTER = "Your_UUID_Filter" // Replace with your specific UUID
        private const val NOTIFICATION_CHANNEL_ID = "BleScanServiceChannel"
        private const val TAG = "BleScanService"

        private const val NOTIFICATION_ID = 1
        const val ALARM_INTERVAL_MILLIS = 5 * 60 * 1000 // Set the interval in milliseconds (5 minutes in this example)
    }

    private val alarmHandler = Handler()
    private val alarmRunnable = Runnable {
        // Restart the foreground service periodically after the specified interval
        startForegroundService()
    }

    // Use lateinit var for wakeLock
    private lateinit var wakeLock: PowerManager.WakeLock
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(applicationContext)
        val scanFilter = ScanFilter.Builder().setServiceUuid(parcelUuid).build()
        scanFilters.add(scanFilter)
//
//        val scanFilter = ScanFilter.Builder().setServiceUuid(parcelUuid).build()
//        scanFilters.add(scanFilter)

        // Initialize wakeLock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BleScanService::WakeLock")

    }

//    private var wakeLock: PowerManager.WakeLock by lazy {
//        var powerManager = getSystemService(Context.POWER_SERVICE)
//                as PowerManager
//        powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BleScanService::WakeLock")
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Acquire the wake lock
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(applicationContext)
        startForegroundService()
        return START_STICKY
    }

//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }

    override fun onDestroy() {
        super.onDestroy()
//        stopForegroundService()
    }

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
            if(mScanResult!!.isEmpty()){
                Log.d(
                    TAG,
                    "AWS CONFIG NO DEVICE FOUND"
                )
                awsConfig!!.publishDeviceName("No device found")
                return
            }

            Log.d("BleScanService","Scan result +$result")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(
                    TAG,
                    "Device " + result!!.device.address + " rssi: " + result.rssi + " Tx: " + result.txPower
                )
                if(result.rssi <= -20  && result.rssi > -70   ){
                    Log.d(
                        TAG,
                        "Device " + result.device.name + "address :"+ result.device.address +" rssi: " + result.rssi + " Onside if"
                    )
                    if(result.device.name != null) {
//                     AddBleDeviceFragment().awsConfig!!.publishDeviceName("BT-Beacon_room1")
                        awsConfig!!.publishDeviceName("BT-Beacon_room1")
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
                    awsConfig!!.publishDeviceName("No device found")
                    AddBleDeviceFragment.receivedNearestDeviceName = ""
                }

            }

        }
    })

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "BLE Scanning Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("BLE Scanning Service")
            .setContentText("Scanning for BLE devices")
            .setSmallIcon(R.drawable.baseline_bluetooth_searching_24)
            .build()
    }

}
