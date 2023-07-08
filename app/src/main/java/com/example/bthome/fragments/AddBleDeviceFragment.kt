package com.example.bthome.fragments

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Bluetooth.HandleBluetooth
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.viewModels.AddBleDeviceViewModel
import java.util.ArrayList


class AddBleDeviceFragment : Fragment() {

    companion object {
        fun newInstance() = AddBleDeviceFragment()
        lateinit var dialog: CustomDialog
    }

    private var handleBluetooth: HandleBluetooth? = null
    var awsConfig: AwsConfigClass? = null
    var nearestDevice: ScanResult? = null


    private lateinit var viewModel: AddBleDeviceViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_ble_device, container, false)
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
//        dialog = CustomDialog(requireContext())
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AddBleDeviceViewModel::class.java
        )
        dialog = CustomDialog(requireContext())
        // TODO: Use the ViewModel
    }

    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(requireContext(), requireActivity())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomDialog(requireContext())

        val scanButton: Button = view.findViewById(R.id.addBleDeviceBtn)
        scanButton.setOnClickListener {
            Log.d("Permission", "Required Permission : ${permissions.size}")
            permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
            permissionHandler.checkStatuses()
            if (permissionHandler.isAllPermissionsEnabled()) {
                if (handleBluetooth == null) {
                    handleBluetooth = HandleBluetooth()
                }
                handleBluetooth!!.scanLeDevices(requireActivity())
                dialog = CustomDialog(requireContext())
                dialog.showDialog(awsConfig!!)
            }
        }



    }

    fun precessScanResult(
        mScanResult: ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?
    ): Int {
        if (result.device.name == null) {
            dialog.updateProgress("UnknownDevice", result.device.address)
        } else {
            dialog.updateProgress(result.device.name, result.device.address)
        }
        var nearestDeviceIndex = 0
        var hasDeviceInRange = false
        //last nearest device
        //check rssi
        //if different update device
        for (i in mScanResult.indices) {
            if (mScanResult[i].rssi >= -65) {
                hasDeviceInRange = true
            }
        }
        if (!hasDeviceInRange) {
            aws!!.publishData("No device found in range", SET_CONFIG)
        }
        return if (mScanResult.size == 1) {
            Log.d("Inside if", "" + result.rssi)
            nearestDevice = result
            if (Math.abs(mScanResult[0].rssi) > 60) {
                nearestDeviceIndex = 404
            }
            nearestDeviceIndex
        } else {
            Log.d("Nearest Device ", "$nearestDevice")
            nearestDevice = result
            Log.d("Nearest Device ", "$nearestDevice")
            Log.d(
                "MATH",
                "" + Math.abs(result.rssi) + " ABC " + Math.abs(nearestDevice!!.getRssi())
            )
            if (Math.abs(result.rssi) < Math.abs(nearestDevice!!.getRssi()) && Math.abs(result.rssi) <= 60) {
                Log.d("Inside else if", "" + result.rssi)
                nearestDevice = result
                for (i in mScanResult.indices) {
                    if (mScanResult[i].device.address.equals(
                            result.device.address,
                            ignoreCase = true
                        )
                    ) {
                        nearestDeviceIndex = i
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
                    if (Math.abs(mScanResult[i].rssi) <= 60) {
                        nearestDevice = mScanResult[i]
                    }
                    if (mScanResult[i].device.address.equals(
                            nearestDevice!!.getDevice().getAddress(),
                            ignoreCase = true
                        ) && Math.abs(
                            mScanResult[i].rssi
                        ) <= 60
                    ) {
                        nearestDeviceIndex = i
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

//         dialog.updateProgress(result.device.name,result.device.address)
    }

}
