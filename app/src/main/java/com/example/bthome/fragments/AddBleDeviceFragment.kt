package com.example.bthome.fragments

import Adapter.ItemClickListener
import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import Data.ResponseData
import Database.DatabaseHelper
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import java.util.ArrayList


class AddBleDeviceFragment : Fragment(), LeScanCallback.DeviceFound, ItemClickListener {

    var awsConfig: AwsConfigClass? = null


    //    grid layout
    private lateinit var gridView: GridView
    companion object {
        lateinit var responseAdapter: ResponseAdapter
        var receivedNearestDeviceName = ""
        var processedScanResultIndex = 0
        @SuppressLint("StaticFieldLeak")
        lateinit var dialog: CustomDialog
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_ble_device, container, false)
// complete screen
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        dialog = CustomDialog(requireContext())

        val scanButton: ImageButton = view.findViewById(R.id.addBleDeviceBtn)
        val moreButton: Button = view.findViewById(R.id.moreButton)
        scanButton.setOnClickListener {
            Log.d("Permission", "Required Permission : ${permissions.size}")
            permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
            permissionHandler.checkStatuses()
            if (permissionHandler.isAllPermissionsEnabled()) {
                if (handleBluetooth == null) {
                    handleBluetooth = HandleBluetooth()
                }
                handleBluetooth!!.scanLeDevices(this)
               dialog = CustomDialog(requireContext())
                dialog.showDialog(awsConfig!!)
            }

        }

        moreButton.setOnClickListener {
//            Log.d("MainActivity", "Clicked item ID: $itemId")
//            val intent = Intent(this, MoreActivity::class.java)
//            startActivity(intent)
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_moreFragment)

        }
        gridView = view.findViewById(R.id.gridView)

        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = ResponseAdapter(emptyList(),this, awsConfig!! )
        gridView.adapter = responseAdapter

        // Fetch the initial data from the database and update the adapter

        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
        setupGridView()
        return  view
    }
    fun blueTooth(){
        Log.d("Permission", "Required Permission : ${permissions.size}")
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {
            if (handleBluetooth == null) {
                handleBluetooth = HandleBluetooth()
            }
            handleBluetooth!!.scanLeDevices(this)
            dialog = CustomDialog(requireContext())
            dialog.showDialog(awsConfig!!)
        }
    }
    override fun onItemClick(itemId: Long) {
        // Handle the item click here
        Log.d("MainActivity", "Clicked item ID: $itemId")

        Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_bleScanResultFragment)
//        val intent = Intent(this, InformationActivity::class.java)
//        startActivity(intent)
    }
    @SuppressLint("MissingInflatedId")
    override fun onItemLongClick(itemId: Long) {
        // Handle the item long click here
        Log.d("MainActivity", "Long clicked item ID: $itemId")

        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_layout, null)
        dialogBuilder.setView(dialogView)

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

        dialogBuilder.setTitle("Select Option")
        dialogBuilder.setPositiveButton("OK") { dialog, whichButton ->
            val selectedOptionId = radioGroup.checkedRadioButtonId
            val selectedOption = dialogView.findViewById<RadioButton>(selectedOptionId)?.text.toString()
            when (selectedOption) {
                "Update" -> {

                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    val inflater = this.layoutInflater
                    val dialogView = inflater.inflate(R.layout.popup_window, null)
                    dialogBuilder.setView(dialogView)

                    val editText = dialogView.findViewById<EditText>(R.id.editText)

                    dialogBuilder.setTitle("Edit Item")
                    dialogBuilder.setPositiveButton("Save") { dialog, whichButton ->
                        val newText = editText.text.toString()
                        // Handle the updated text as needed
                        // For example, update the data in the database using dbHelper

                        var newRD  = ResponseData("topic",newText,"")

                        DatabaseHelper(requireContext()).updateResponseData(responseAdapter.getString(itemId.toInt()).toString(),newRD)
                        Toast.makeText(requireContext(), "Update option selected", Toast.LENGTH_SHORT).show()
                        setupGridView()
                    }
                    dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
                        // Do nothing or perform any desired action on cancel
                    }

                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()

                    true

                }
                "Delete" -> {
                    // Perform delete logic here
                    Log.d(
                        "MainActivity",
                        "ON DELETE + ${responseAdapter.getString(itemId.toInt()).toString()}"
                    )
                    DatabaseHelper(requireContext()).deleteResponseData(responseAdapter.getString(itemId.toInt()).toString())
                    Toast.makeText(requireContext(), "Delete option selected", Toast.LENGTH_SHORT).show()
                    setupGridView()
                }
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
            // Do nothing or perform any desired action on cancel
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    public override fun onResume() {
        super.onResume()
        Log.d("Main activity", "on resume")
        blueTooth()
        setupGridView()
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = ResponseAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        gridView.adapter = responseAdapter

    }

    private var handleBluetooth: HandleBluetooth? = null
    var nearestDevice: ScanResult? = null


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

    fun precessScanResult(
        mScanResult: java.util.ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?,
    ): Int {
        if (result.device.name == null) {
            AddBleDeviceFragment.dialog.updateProgress("UnknownDevice", result.device.address)
        } else {
            AddBleDeviceFragment.dialog.updateProgress(result.device.name, result.device.address)
        }
        var nearestDeviceIndex = 0
        var hasDeviceInRange = false
        //last nearest device
        //check rssi
        //if different update device
        for (i in mScanResult.indices) {

            if (mScanResult[i].rssi >= -100) {
                hasDeviceInRange = true
                Log.d("ANDRD_DEV 1", " ${mScanResult[i].rssi}")
                Log.d("ANDRD_DEV 2", " ${mScanResult[i].device.address}")
                Log.d("ANDRD_DEV 3", " ${mScanResult[i].scanRecord}")
            }
        }

        if (!hasDeviceInRange) {
            receivedNearestDeviceName = ""
            aws!!.publishData("No device found in range", AwsConfigConstants.SET_CONFIG)
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


    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
//        TODO("Not yet implemented")
        setupGridView()
        processedScanResultIndex = precessScanResult(mScanResult!!, result!!,awsConfig)
        Log.d("POSITION ", "" + processedScanResultIndex)

    }

}
