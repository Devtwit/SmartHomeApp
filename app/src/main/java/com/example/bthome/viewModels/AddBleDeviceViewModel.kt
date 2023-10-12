package com.example.bthome.viewModels

import AwsConfigThing.AwsConfigClass
import Data.DeviceSelection
import Data.ResponseData
import DatabaseHelper
import android.annotation.SuppressLint
import android.app.Dialog

import android.bluetooth.le.ScanResult
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface

import androidx.core.content.ContextCompat

import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.amazonaws.auth.policy.Resource
import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.isFanPref
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.isLightPref
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.publishStatus
import com.example.bthome.fragments.SearchLocationFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddBleDeviceViewModel : ViewModel(){
    private val selectedDevices = mutableListOf<DeviceSelection>()
    var hasDeviceInRange = false
    var hasDeviceInRangeId = -1
    companion object{
        private val TAG = AddBleDeviceViewModel::class.java.simpleName
    }
    @SuppressLint("SuspiciousIndentation")
    fun precessScanResult(
        mScanResult: java.util.ArrayList<ScanResult>,
        result: ScanResult,
        aws: AwsConfigClass?,
        context: Context,
    ): Int {
        val responseDataList = mutableListOf<ResponseData>()
//        var hasDeviceInRange = false
        //last nearest device
        //check rssi
        //if different update device
        val dbHelper = DatabaseHelper(context)
        val initialData = dbHelper.getAllResponseData()
        Log.d(TAG," $responseDataList")
        Log.d(TAG," ${initialData} ${initialData.isNotEmpty()}")
        if(initialData.isNotEmpty())
        Log.d(TAG," ${initialData.get(0).location}")

        for (i in mScanResult.indices) {

            if (mScanResult[i].rssi >= -75) {
                hasDeviceInRange = true
//                Toast.makeText(context,"RSSI : ${mScanResult[i].rssi}", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(context,"RSSI : ${mScanResult[i].rssi}", Toast.LENGTH_LONG).show()
            }
        }

        if (!hasDeviceInRange) {
            Log.d(TAG, "" + result.rssi + " " + publishStatus)
            AddBleDeviceFragment.receivedNearestDeviceName = ""
//            aws!!.publishData("No device found", AwsConfigConstants.SET_CONFIG)
            if(initialData.isNotEmpty()) {
                if (publishStatus.equals("BT-Beacon_room1") || publishStatus.equals("status")) {
                    aws!!.publishDeviceNameOff("BT-Beacon_room1")
                    publishStatus = "No device found"
                }
            }

        }
        else if(initialData.isNotEmpty()){
//          else{
                if (publishStatus.equals("No device found") || publishStatus.equals("status")) {
//              if(initialData.get(0).location.equals("BT-Beacon_room1")) {
                Log.d(TAG, "" + result.rssi + " " + publishStatus)
//            aws!!.publishData("BT-Beacon_room1", AwsConfigConstants.SET_CONFIG)
                if (publishStatus.equals("No device found") || publishStatus.equals("status")) {
//                    aws!!.publishDeviceName("BT-Beacon_room1")
                    hasDeviceInRangeId= dbHelper.getLocationIdByAddress(initialData.get(0).address).toInt()
                    if(isFanPref && isLightPref){
                        Log.d(TAG, "Both Fan and Light selected")
                        aws!!.publishDeviceName("BT-Beacon_room1")
                    } else if(isLightPref){
                        Log.d(TAG, "Only Light selected")
//                        aws!!.publishDeviceNameLightOn("BT-Beacon_room1")
                        aws!!.publishDeviceTurnOnLight("BT-Beacon_room1")
                    } else if(isFanPref){
                        Log.d(TAG, "Only Fan selected")
//                        aws!!.publishDeviceNameFanOn("BT-Beacon_room1")
                        aws!!.publishDeviceTurnOnFan("BT-Beacon_room1")
                    } else{
                        Log.d(TAG, "Not selected")
//                        aws!!.publishDeviceName("BT-Beacon_room1")
                        aws!!.publishDeviceNameOff("BT-Beacon_room1")
                    }
                    publishStatus = "BT-Beacon_room1"

                }
            }
        }
        // Update nearestDevice
        return 0
    }
    fun showLocationDialog(context: Context, awsConfig: AwsConfigClass?) {
        val dbHelper = DatabaseHelper(context)
        val responseDataList = dbHelper.getAllResponseData()

        val locationNames = responseDataList.map { it.location }

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Select a Location")

        dialogBuilder.setItems(locationNames.toTypedArray()) { _, position ->
            val selectedLocation = locationNames[position]

            handleLocationSelection(selectedLocation,context,awsConfig)
            handleSelectedDevices(selectedLocation,context,awsConfig)
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


    private fun handleLocationSelection(
        selectedLocation: String,
        context: Context,
        awsConfig: AwsConfigClass?
    ) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.dialog_preferences)
        dialog.setTitle("Select Preferences")

        val storedDevices = loadStoredDevices(selectedLocation, context)
        val fanItemLayout = dialog.findViewById<LinearLayout>(R.id.fanLayout)
        val fanImageButton = dialog.findViewById<ImageButton>(R.id.fanImageButton)
        val fanTextView = dialog.findViewById<TextView>(R.id.fanTextView)
        val lightItemLayout = dialog.findViewById<LinearLayout>(R.id.lightLayout)
        val lightImageButton = dialog.findViewById<ImageButton>(R.id.lightImageButton)
        val lightTextView = dialog.findViewById<TextView>(R.id.lightTextView)
        var fanisSelected = false
        var lightisSelected = false

        fanItemLayout!!.setOnClickListener {
            fanisSelected = !fanisSelected
            if (fanisSelected) {
                fanTextView!!.setTextColor(ContextCompat.getColor(context, R.color.black))
                fanItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_device_orange))
                fanImageButton!!.setImageResource(R.drawable.fan_on)
            } else {
                fanItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.select_device_unselected_bg))
                fanImageButton!!.setImageResource(R.drawable.baseline_toys_24)
            }
        }
//
        lightItemLayout!!.setOnClickListener {
            lightisSelected = !lightisSelected
            if (lightisSelected) {
                lightTextView!!.setTextColor(ContextCompat.getColor(context, R.color.black))
                lightItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_device_orange))
                lightImageButton!!.setImageResource(R.drawable.baseline_lightbulb_black)
            } else {
                lightItemLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.select_device_unselected_bg))
                lightImageButton!!.setImageResource(R.drawable.custom_checkbox_light)

            }

        }


        dialog.findViewById<Button>(R.id.okButton)!!.setOnClickListener {
            // Update the selectedDevices list with the new selections
            selectedDevices.clear()
            if (fanisSelected) {
                selectedDevices.add(DeviceSelection("Fan", true))
            }

            if (lightisSelected) {
                selectedDevices.add(DeviceSelection("Light", true))
            }


            // Save the selected preferences to persistent storage for the chosen location
            saveSelectedDevices(selectedLocation, selectedDevices, context)
            handleSelectedDevices(selectedLocation,context, awsConfig )
            dialog.dismiss() // Close the dialog
        }

        dialog.findViewById<Button>(R.id.cancelButton)!!.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }

        dialog.show()
    }


    private fun saveSelectedDevices(selectedLocation: String, selectedDevices: List<DeviceSelection>,context: Context) {
        // Save the selected devices to persistent storage for the chosen location
        val sharedPrefs = context.getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val gson = Gson()
        val json = gson.toJson(selectedDevices)
        editor.putString(selectedLocation, json)
        editor.apply()
    }


    private fun loadStoredDevices(selectedLocation: String,context: Context): List<DeviceSelection> {
        val sharedPrefs = context.getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(selectedLocation, null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }
    private fun handleSelectedDevices(
        selectedLocation: String,
        context: Context,
        awsConfig: AwsConfigClass?
    ) {
        val sharedPrefs = context.getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(selectedLocation, null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            val storedDevices = gson.fromJson<List<DeviceSelection>>(json, type)

            val fanSelected = storedDevices.any { it.deviceId == "Fan" && it.isSelected }
            val lightSelected = storedDevices.any { it.deviceId == "Light" && it.isSelected }

            when {
                fanSelected && lightSelected -> {
                    // Both Fan and Light are selected, perform specific action
                    isLightPref = true
                    isFanPref = true
                    awsConfig!!.publishDeviceName("BT-Beacon_room1")
                }
                fanSelected -> {
                    // Only Fan is selected, perform Fan selected action
                    isLightPref = false
                    isFanPref = true
                    awsConfig!!.publishDeviceTurnOnFan("BT-Beacon_room1")
                }
                lightSelected -> {
                    // Only Light is selected, perform Light selected action
                    isFanPref = false
                    isLightPref = true
                    awsConfig!!.publishDeviceTurnOnLight("BT-Beacon_room1")
                }
                else -> {
                    isFanPref = false
                    isLightPref = false
                    awsConfig!!.publishDeviceNameOff("BT-Beacon_room1")
                    // None selected or other cases, handle accordingly
                }
            }
        } else {
            // Handle the case when no preferences are stored for the selected location
        }
    }

}
