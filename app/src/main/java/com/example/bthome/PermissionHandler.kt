package com.example.bthome

//import Database.DatabaseHelper
import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager

import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.bthome.fragments.AddBleDeviceFragment
import com.example.bthome.fragments.MoreFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class PermissionHandler( private val context: Context,private val activity: Activity) {


    var multipleRequest :Boolean = false

    fun checkPermission(permission: String): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private var customPopUp: Dialog? = Dialog(context)
    private var appSettingPopup: Dialog? = Dialog(context)

    fun checkMultiplePermissions(permissions: Array<String>): Array<String> {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (!checkPermission(permission)) {
                permissionsToRequest.add(permission)
            }
        }
        return permissionsToRequest.toTypedArray()
    }

    fun requestMultiplePermissions(permissions: Array<String>, requestCode: Int) {
        val permissionsToRequest = checkMultiplePermissions(permissions)
        if(permissionsToRequest.isNotEmpty()) {
//            CustomDialog(context).showAppSettingsDialog()

            appSettingPopup = CustomDialog(context).openSystemSettingPopup(context,object :
                ThreeButtonsListener {
                override fun onOkButtonClicked() {
                    if(permissionsToRequest.contains( Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)){
                        CustomDialog(context).openBackgroundPermission()
                    } else {
                        CustomDialog(context).openAppSettings()
                    }

                    appSettingPopup?.dismiss()
                }

                override fun onCancelButtonClicked() {
                    super.onCancelButtonClicked()
                    appSettingPopup?.dismiss()
                }
            })
                appSettingPopup?.show()
        }
        else
            multipleRequest = true
    }

    fun isAllPermissionsEnabled() : Boolean{
        return isBluetoothEnabled() && isLocationEnabled() && isInternetEnabled() && multipleRequest
    }

    fun checkStatuses() {
        val bluetoothEnabled = isBluetoothEnabled()
        val locationEnabled = isLocationEnabled()
        val internetEnabled = isInternetEnabled()
//        val wifiEnabled = isWifiEnabled()

        val message = StringBuilder()
        message.append("Bluetooth: ")
            .append(if (bluetoothEnabled) "Enabled" else "Disabled")
            .append("\n")
        message.append("Location (GPS): ")
            .append(if (locationEnabled) "Enabled" else "Disabled")
            .append("\n")
//        message.append("Wi-Fi: ")
//            .append(if (wifiEnabled) "Enabled" else "Disabled")
        message.append("Internet : ")
            .append(if (internetEnabled) "Enabled" else "Disabled")
            .append("\n")
            .append("To Scan device all Permissions must be Enabled ")


        customPopUp = CustomDialog(context).buildTurnOffAlertPopup(context, message.toString(),!(isLocationEnabled() && isBluetoothEnabled() && isInternetEnabled()),object :
            ThreeButtonsListener {
            override fun onOkButtonClicked() {
                customPopUp?.dismiss()
            }

            override fun onCancelButtonClicked() {
                super.onCancelButtonClicked()
                customPopUp?.dismiss()
            }
        })
        if(!(isLocationEnabled() && isBluetoothEnabled() && isInternetEnabled()))
        customPopUp?.show()

    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isInternetEnabled(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }


    private fun isWifiEnabled(): Boolean {
        val wifiManager = activity.getSystemService(WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}
