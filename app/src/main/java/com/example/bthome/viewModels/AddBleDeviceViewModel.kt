package com.example.bthome.viewModels

import AwsConfigThing.AwsConfigClass
import Bluetooth.LeScanCallback
import Data.ResponseData
import DatabaseHelper
import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bthome.fragments.AddBleDeviceFragment
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.isFanPref
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.isLightPref
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.publishStatus

class AddBleDeviceViewModel :ViewModel(){

    @SuppressLint("SuspiciousIndentation")
    fun precessScanResult(
        mScanResult: java.util.ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?,
        context: Context,
    ): Int {
        val responseDataList = mutableListOf<ResponseData>()
        var nearestDeviceIndex = 0
        var hasDeviceInRange = false
        //last nearest device
        //check rssi
        //if different update device
        val dbHelper = DatabaseHelper(context)
        val initialData = dbHelper.getAllResponseData()
        Log.d("Response Data list"," $responseDataList")
        Log.d("Response Data list initialData"," ${initialData} ${initialData.isNotEmpty()}")
        if(initialData.isNotEmpty())
        Log.d("Response Data list initialData"," ${initialData.get(0).location}")

        for (i in mScanResult.indices) {

            if (mScanResult[i].rssi >= -75) {
                hasDeviceInRange = true
//                Log.d("ANDRD_DEV 1", " ${mScanResult[i].rssi}")
//                Log.d("ANDRD_DEV 2", " ${mScanResult[i].device.address}")
//                Log.d("ANDRD_DEV 3", " ${mScanResult[i].scanRecord}")
                Toast.makeText(context,"RSSI : ${mScanResult[i].rssi}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context,"RSSI : ${mScanResult[i].rssi}", Toast.LENGTH_LONG).show()
            }
        }

        if (!hasDeviceInRange) {
            Log.d("Range_No ", "" + result.rssi + " " + publishStatus)
            AddBleDeviceFragment.receivedNearestDeviceName = ""
//            aws!!.publishData("No device found", AwsConfigConstants.SET_CONFIG)
            if(initialData.isNotEmpty()) {
//                if (publishStatus.equals("BT-Beacon_room1") || publishStatus.equals("status")) {
                    aws!!.publishDeviceNameOff("BT-Beacon_room1")
                    publishStatus = "No device found"
//                }
            }

        }
        else if(initialData.isNotEmpty()){
//          else{

              if(initialData.get(0).location.equals("BT-Beacon_room1")) {
                Log.d("Range has device ", "" + result.rssi + " " + publishStatus)
//            aws!!.publishData("BT-Beacon_room1", AwsConfigConstants.SET_CONFIG)
//                if (publishStatus.equals("No device found") || publishStatus.equals("status")) {
//                    aws!!.publishDeviceName("BT-Beacon_room1")

                    if(isFanPref && isLightPref){
                        Log.d("SelectedDevices", "Both Fan and Light selected")
                        aws!!.publishDeviceName("BT-Beacon_room1")
                    } else if(isLightPref){
                        Log.d("SelectedDevices", "Only Light selected")
//                        aws!!.publishDeviceNameLightOn("BT-Beacon_room1")
                        aws!!.publishDeviceTurnOnLight("BT-Beacon_room1")
                    } else if(isFanPref){
                        Log.d("SelectedDevices", "Only Fan selected")
//                        aws!!.publishDeviceNameFanOn("BT-Beacon_room1")
                        aws!!.publishDeviceTurnOnFan("BT-Beacon_room1")
                    } else{
                        Log.d("SelectedDevices", "Not selected")
                        aws!!.publishDeviceName("BT-Beacon_room1")
                    }
                    publishStatus = "BT-Beacon_room1"

                }
//            }
        }
        // Update nearestDevice
        return 0
    }

}
