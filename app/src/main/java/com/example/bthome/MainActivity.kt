package com.example.bthome

import AwsConfigThing.AwsConfigClass
import Bluetooth.LeScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.example.bthome.fragments.AddBleDeviceFragment

class MainActivity : AppCompatActivity(), LeScanCallback.DeviceFound {

    var awsConfig: AwsConfigClass? = null
//    var nearestDevice: ScanResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// complete screen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(this)

    }

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
//        TODO("Not yet implemented")
        val processedScanResultIndex: Int = AddBleDeviceFragment().precessScanResult(mScanResult!!, result!!,awsConfig)
        Log.d("POSITION ", "" + processedScanResultIndex)
    }

}