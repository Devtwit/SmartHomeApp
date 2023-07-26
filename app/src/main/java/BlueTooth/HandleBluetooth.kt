package Bluetooth

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.UUID_FILTER
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.support.v4.app.ActivityCompat
import android.util.Log
import java.util.UUID

class HandleBluetooth(val cont: Context,val awsConfig: AwsConfigClass?) {

    var leScanCallback: LeScanCallback? = null
    var bluetoothLeScanner: BluetoothLeScanner? = null
    var parcelUuid: ParcelUuid? = null
    var isScanning = false

    /*
    To start LE scanning if already scanning then stop ongoing scanning
    */
    fun scanLeDevices(context: LeScanCallback.DeviceFound) {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }
        if (!bluetoothAdapter!!.isEnabled) {
            Log.d(TAG, "Please turn on bluetooth first")
            return
        }

        val handler = Handler()
        val uuid = UUID.fromString(UUID_FILTER)
        if (parcelUuid == null) {
            parcelUuid = ParcelUuid(uuid)
        }
        Log.d(TAG, "leScanCallback : $leScanCallback")
        if (leScanCallback == null) {
            leScanCallback = LeScanCallback(context)
        }
        if (bluetoothLeScanner == null) {
            bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
        }
        val scanFilters: MutableList<ScanFilter> = ArrayList()
        val scanFilter = ScanFilter.Builder().setServiceUuid(parcelUuid).build()
        scanFilters.add(scanFilter)
        val scanSettings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        handler.post(Runnable {

//            if (!isScanning) {
                if (ActivityCompat.checkSelfPermission(
                        cont,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "No access to scan")
                    return@Runnable
                }
                bluetoothLeScanner!!.startScan(scanFilters, scanSettings, leScanCallback)
                isScanning = true
//            }
//            Stop Scanning
//            else {
//                bluetoothLeScanner!!.stopScan(leScanCallback)
//                Log.d(TAG, "Scanning Stopped")
//                isScanning = false
//                leScanCallback!!.mScanResult = ArrayList()
////                                    bluetoothLeScanner!!.startScan(scanFilters, scanSettings, leScanCallback);
//            }
        })

// Set a timeout to stop scanning if no matching device is found within the specified time
//        timeoutHandler.postDelayed({
//            Log.d(TAG,"Scan stopped called")
//            awsConfig!!.publishDeviceName("No device found")
//        }, scanTimeout)
    }

    companion object {
        private val TAG = HandleBluetooth::class.java.simpleName
        var bluetoothAdapter: BluetoothAdapter? = null
        var scanning = false
   val timeoutHandler = Handler(Looper.getMainLooper())
        private val scanTimeout: Long = 10000 // 10 seconds (adjust as needed)

    }
}