package com.example.bthome

import Adapter.ItemClickListener
import AwsConfigThing.AwsConfigClass
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import android.Manifest
import android.bluetooth.le.ScanResult
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.bthome.fragments.AddBleDeviceFragment
import java.util.ArrayList

class SmartActivity : AppCompatActivity() , LeScanCallback.DeviceFound {
    var awsConfig: AwsConfigClass? = null
    private var handleBluetooth: HandleBluetooth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart)

        // complete screen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(this)
    }

    fun blueTooth(){
        Log.d("Permission", "Required Permission : ${permissions.size}")
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {
            if (handleBluetooth == null) {
                handleBluetooth = HandleBluetooth(this)
            }
            handleBluetooth!!.scanLeDevices(this)
        }
    }
    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
//        TODO("Not yet implemented")
//        setupGridView()
          precessScanResult(mScanResult!!, result!!,awsConfig)
//        Log.d("POSITION ", "" + AddBleDeviceFragment.processedScanResultIndex)

    }
    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(this, this)
    }

    private val REQUEST_PERMISSION_CODE = 1
    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE,
    )

    fun precessScanResult(
        mScanResult: java.util.ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?,
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



//        return if (mScanResult.size == 1) {
//            Log.d("Inside if", "" + result.rssi)
//            Log.d("ANDRD_DEV 4", "" + result.rssi)
//            nearestDevice = result
////            receivedNearestDeviceName = result.device.address
//            Log.d("ANDRD_DEV 1", " ${result.rssi}")
//            Log.d("ANDRD_DEV 2", " ${result.device.address}")
//            Log.d("ANDRD_DEV 3", " ${result.scanRecord}")
//            Log.d("ANDRD_DEV 5", " ${nearestDevice}")
//            Log.d("ANDRD_DEV 6", " ${result}")
//            if (Math.abs(mScanResult[0].rssi) > 100) {
//                nearestDeviceIndex = 404
//            }
//            nearestDeviceIndex
//        } else {
//            Log.d("Nearest Device ", "$nearestDevice")
//            nearestDevice = result
////            receivedNearestDeviceName = result.device.address
//            Log.d("Nearest Device ", "$nearestDevice")
//            Log.d("Nearest Device ", "$nearestDevice")
//            Log.d(
//                "MATH",
//                "" + Math.abs(result.rssi) + " ABC " + Math.abs(nearestDevice!!.getRssi())
//            )
//            if (Math.abs(result.rssi) < Math.abs(nearestDevice!!.getRssi()) && Math.abs(result.rssi) <= 100) {
//                Log.d("Inside else if", "" + result.rssi)
//                nearestDevice = result
////                receivedNearestDeviceName = result.device.address
//                for (i in mScanResult.indices) {
//                    if (mScanResult[i].device.address.equals(
//                            result.device.address,
//                            ignoreCase = true
//                        )
//                    ) {
//                        nearestDeviceIndex = i
////                        receivedNearestDeviceName = nearestDevice!!.device.address
//                        Log.d("Matched", "" + nearestDeviceIndex)
//                        break
//                    }
//                }
//                if (nearestDeviceIndex == 0) {
//                    nearestDeviceIndex = 404
//                }
//                nearestDeviceIndex
//            } else {
//                for (i in mScanResult.indices) {
//                    if (Math.abs(mScanResult[i].rssi) <= 100) {
////                        nearestDevice = mScanResult[i]
////                        receivedNearestDeviceName = nearestDevice!!.device.address
//                    }
//                    if (mScanResult[i].device.address.equals(
//                            nearestDevice!!.getDevice().getAddress(),
//                            ignoreCase = true
//                        ) && Math.abs(
//                            mScanResult[i].rssi
//                        ) <= 100
//                    ) {
//                        nearestDeviceIndex = i
////                        receivedNearestDeviceName = nearestDevice!!.device.address
//                        Log.d("Matched", "" + nearestDeviceIndex)
//                        break
//                    }
//                }
//                if (nearestDeviceIndex == 0) {
//                    nearestDeviceIndex = 404
//                }
//                nearestDeviceIndex
//            }
//        }
        return  404
    }
}