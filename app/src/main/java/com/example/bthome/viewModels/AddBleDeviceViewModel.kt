package com.example.bthome.viewModels

import AwsConfigThing.AwsConfigClass
import android.arch.lifecycle.ViewModel
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bthome.fragments.AddBleDeviceFragment

class AddBleDeviceViewModel :ViewModel(){
    var nearestDevice: ScanResult? = null
    fun precessScanResult(
        mScanResult: java.util.ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?,
        context: Context,
    ): Int {
        var nearestDeviceIndex = 0
        var hasDeviceInRange = false
        //last nearest device
        //check rssi
        //if different update device
        for (i in mScanResult.indices) {

            if (mScanResult[i].rssi >= -70) {
                hasDeviceInRange = true
                Log.d("ANDRD_DEV 1", " ${mScanResult[i].rssi}")
                Log.d("ANDRD_DEV 2", " ${mScanResult[i].device.address}")
                Log.d("ANDRD_DEV 3", " ${mScanResult[i].scanRecord}")
                Toast.makeText(context,"RSSI : ${mScanResult[i].rssi}", Toast.LENGTH_LONG).show()
            }
        }

        if (!hasDeviceInRange) {
            Log.d("Range_No ", "" + result.rssi)
            AddBleDeviceFragment.receivedNearestDeviceName = ""
//            aws!!.publishData("No device found", AwsConfigConstants.SET_CONFIG)
            aws!!.publishDeviceName("No device found")

        } else {
            Log.d("Range has device ", "" + result.rssi)
//            aws!!.publishData("BT-Beacon_room1", AwsConfigConstants.SET_CONFIG)
            aws!!.publishDeviceName("BT-Beacon_room1")
        }



        return if (mScanResult.size == 1) {
            Log.d("Inside if", "" + result.rssi)
            Log.d("ANDRD_DEV 4", "" + result.rssi)
            nearestDevice = result
//            receivedNearestDeviceName = result.device.address
            Log.d("ANDRD_DEV 1", " ${result.rssi}")
            Log.d("ANDRD_DEV 2", " ${result.device.address}")
            Log.d("ANDRD_DEV 3", " ${result.scanRecord}")
            Log.d("ANDRD_DEV 5", " ${nearestDevice}")
            Log.d("ANDRD_DEV 6", " ${result}")
            if (Math.abs(mScanResult[0].rssi) > 100) {
                nearestDeviceIndex = 404
            }
            nearestDeviceIndex
        } else {
            Log.d("Nearest Device ", "$nearestDevice")
            nearestDevice = result
//            receivedNearestDeviceName = result.device.address
            Log.d("Nearest Device ", "$nearestDevice")
            Log.d("Nearest Device ", "$nearestDevice")
            Log.d(
                "MATH",
                "" + Math.abs(result.rssi) + " ABC " + Math.abs(nearestDevice!!.getRssi())
            )
            if (Math.abs(result.rssi) < Math.abs(nearestDevice!!.getRssi()) && Math.abs(result.rssi) <= 100) {
                Log.d("Inside else if", "" + result.rssi)
                nearestDevice = result
//                receivedNearestDeviceName = result.device.address
                for (i in mScanResult.indices) {
                    if (mScanResult[i].device.address.equals(
                            result.device.address,
                            ignoreCase = true
                        )
                    ) {
                        nearestDeviceIndex = i
//                        receivedNearestDeviceName = nearestDevice!!.device.address
                        Log.d("Matched", "" + nearestDeviceIndex)
                        break
                    }
                }
                if (nearestDeviceIndex == 0) {
                    nearestDeviceIndex = 404
                }
                nearestDeviceIndex
            } else {
                for (i in mScanResult.indices) {
                    if (Math.abs(mScanResult[i].rssi) <= 100) {
                        nearestDevice = mScanResult[i]
//                        receivedNearestDeviceName = nearestDevice!!.device.address
                    }
                    if (mScanResult[i].device.address.equals(
                            nearestDevice!!.getDevice().getAddress(),
                            ignoreCase = true
                        ) && Math.abs(
                            mScanResult[i].rssi
                        ) <= 100
                    ) {
                        nearestDeviceIndex = i
//                        receivedNearestDeviceName = nearestDevice!!.device.address
                        Log.d("Matched", "" + nearestDeviceIndex)
                        break
                    }
                }
                if (nearestDeviceIndex == 0) {
                    nearestDeviceIndex = 404
                }
                nearestDeviceIndex
            }
        }
    }

}
