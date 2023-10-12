package Adapter

import AwsConfigThing.AwsConfigClass
import Data.DeviceSelection
import Data.ResponseData
import DatabaseHelper
import android.bluetooth.BluetoothClass
import android.content.Context

import androidx.core.content.ContextCompat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment
import com.example.bthome.fragments.SplashScreenFragment.Companion.apkContext

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DeviceListAdapter(private val deviceList: List<ResponseData>,
private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {
    var awsConfig: AwsConfigClass? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = deviceList[position]
        holder.bind(device)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectedDevices = mutableListOf<DeviceSelection>()
        fun bind(device: ResponseData) {
            // Bind device data to UI elements in the item layout
            val locationTextView = itemView.findViewById<TextView>(R.id.deviceInfo)
            locationTextView.text = device.location
            awsConfig = AwsConfigClass()
            awsConfig!!.startAwsConfigurations(apkContext)
            // Set an onClickListener for the item
            itemView.setOnClickListener {
                // Handle item click here, open another popup, etc.
//                showPopupForDevice(device)
                val dbHelper = DatabaseHelper(apkContext)
                val responseDataList = dbHelper.getAllResponseData()
                val itemId = getItemId(position)
                itemClickListener.onItemClick(itemId)
                val locationNames = responseDataList.map { it.location }
                val selectedLocation = locationNames[position]
                Toast.makeText(apkContext, "Feature popup item clicked", Toast.LENGTH_SHORT).show()
                handleLocationSelection(selectedLocation,apkContext,awsConfig)
                    handleSelectedDevices(selectedLocation,apkContext,awsConfig)
            }


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
                        AddBleDeviceFragment.isLightPref = true
                        AddBleDeviceFragment.isFanPref = true
                        awsConfig!!.publishDeviceName("BT-Beacon_room1")
                    }
                    fanSelected -> {
                        // Only Fan is selected, perform Fan selected action
                        AddBleDeviceFragment.isLightPref = false
                        AddBleDeviceFragment.isFanPref = true
                        awsConfig!!.publishDeviceTurnOnFan("BT-Beacon_room1")
                    }
                    lightSelected -> {
                        // Only Light is selected, perform Light selected action
                        AddBleDeviceFragment.isFanPref = false
                        AddBleDeviceFragment.isLightPref = true
                        awsConfig!!.publishDeviceTurnOnLight("BT-Beacon_room1")
                    }
                    else -> {
                        AddBleDeviceFragment.isFanPref = false
                        AddBleDeviceFragment.isLightPref = false
                        awsConfig!!.publishDeviceNameOff("BT-Beacon_room1")
                        // None selected or other cases, handle accordingly
                    }
                }
            } else {
                // Handle the case when no preferences are stored for the selected location
            }
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
            dialog.getWindow()?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            dialog.show()
        }
        private fun saveSelectedDevices(selectedLocation: String, selectedDevices: List<DeviceSelection>, context: Context) {
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
    }
    interface ItemClickListener {
        fun onItemClick(device: Long)
    }

}



