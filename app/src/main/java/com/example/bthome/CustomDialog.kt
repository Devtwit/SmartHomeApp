package com.example.bthome

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class CustomDialog(private val context: Context) {
    private var dialog: AlertDialog? = null
    private var progressBar: ProgressBar? = null
    private lateinit var textView1 : TextView
    private lateinit var textView2 : TextView
    companion object{
        lateinit var scannedData : String
    }

    @SuppressLint("MissingInflatedId")
    fun showDialog(awsConfig :AwsConfigClass) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.custom_dialog_layout, null)

         textView1 = dialogView.findViewById<TextView>(R.id.heading)
         textView2 = dialogView.findViewById<TextView>(R.id.deviceName)

        progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        val button1 = dialogView.findViewById<Button>(R.id.button1)
        val button2 = dialogView.findViewById<Button>(R.id.button2)
        textView2.visibility = View.GONE
        builder.setView(dialogView)
        dialog = builder.create()

        button1.setOnClickListener {
            // Handle button 1 click
            dismissDialog()
        }

        button2.setOnClickListener {
            // Handle button 2 click
            // Publish the scanned data to the MQTT topic
            val topic = SET_CONFIG // Replace with your desired MQTT topic
            awsConfig!!.publishData(scannedData, topic)
            dismissDialog()
        }

        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    fun updateProgress(deviceName : String,deviceAddress: String) {
        progressBar!!.visibility = View.GONE
        textView2.visibility = View .VISIBLE

        scannedData = "Name : " +deviceName + "\n" + "Address : " +deviceAddress
        textView2.setText(scannedData)
    }


     fun showAppSettingsDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Permissions are required to use the app. Please enable them in the app settings.")
            .setCancelable(false)
            .setPositiveButton("Settings") { dialog, _ ->
                dialog.dismiss()
                // Redirect to app settings
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Permission")
        alert.show()
    }

    @SuppressLint("SuspiciousIndentation")
    fun showStatusDialog(message: String, isAccess :Boolean) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->

                dialog.dismiss()
            }
            .setNegativeButton("Denied"){dialog,id->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Status")
        if(isAccess)
            alert.show()
    }


    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}
