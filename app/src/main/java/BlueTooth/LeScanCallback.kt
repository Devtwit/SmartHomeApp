package Bluetooth

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log

import com.example.bthome.fragments.AddBleDeviceFragment.Companion.receivedNearestDeviceName


/*
To receive LE scan callback from android.bluetooth.le.ScanCallback
*/
class LeScanCallback(private val deviceFound: DeviceFound) : ScanCallback() {
    private val TAG = LeScanCallback::class.java.simpleName
    var mScanResult: ArrayList<ScanResult>? = ArrayList()
    var isScanStarted = false
//    var deviceFound: DeviceFound  = context as DeviceFound


    interface DeviceFound {
        fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        if (!isScanStarted) {
            isScanStarted = true
            Log.d(TAG, "Scanning Started")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(
                TAG,
                "Device " + result.device.address + " rssi: " + result.rssi + " Tx: " + result.txPower
            )
            if(result.rssi <= -20  && result.rssi > -100   ){
                Log.d(
                    TAG,
                    "Device " + result.device.name + " rssi: " + result.rssi + " Onside if"
                )
                if(result.device.name != null)
                receivedNearestDeviceName = result.device.address
                else
                    receivedNearestDeviceName = result.device.address
            } else {
                receivedNearestDeviceName = ""
            }

        }
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PERMISSION_GRANTED
//        ) {
//            Log.d(TAG, "No access to scan")
//            return
//        }
        if (result.device.name != null || result.device.address != null) {
            if (mScanResult != null && mScanResult!!.size > 0) {
                var hasDevice = false
                for (i in mScanResult!!.indices) {
                    if (result.device != null && result.device.address.equals(
                            mScanResult!![i].device.address, ignoreCase = true
                        )
                    ) {
                        hasDevice = true
                        mScanResult!!.removeAt(i)
                        mScanResult!!.add(i, result)
                        //mScanResult.add(result);
                    }
                }
                if (!hasDevice && result.device != null) {
                    mScanResult!!.add(result)
                }
            } else {
                if (mScanResult != null && result.device != null) {
                    mScanResult!!.add(result)
                }
            }
            deviceFound.scanCompleted(mScanResult, result)
        }
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.d(TAG, "Failed errorCode: $errorCode")
    }

    override fun onBatchScanResults(results: List<ScanResult>) {
        super.onBatchScanResults(results)
    }
}