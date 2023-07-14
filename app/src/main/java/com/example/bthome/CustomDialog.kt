package com.example.bthome

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
import Database.DatabaseHelper
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.net.Uri
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class CustomDialog(private val context: Context) {
    private var dialog: AlertDialog? = null
    private var progressBar: ProgressBar? = null
    private lateinit var textView1 : TextView
    private lateinit var textView2 : TextView
    private lateinit var button1 : Button
    private lateinit var button2  :Button
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
         button1 = dialogView.findViewById<Button>(R.id.button1)
         button2 = dialogView.findViewById<Button>(R.id.button2)
        val button3 = dialogView.findViewById<Button>(R.id.button3)
        textView2.visibility = View.GONE
        button1.visibility = View.GONE
        button2.visibility = View.GONE
        builder.setView(dialogView)
        dialog = builder.create()
        button1.isClickable = false
        button2.isClickable = false
        button1.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
//            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.popup_layout, null)
            dialogBuilder.setView(dialogView)

            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
             var rDelete = dialogView.findViewById<RadioButton>(R.id.radioDelete)
            rDelete.visibility= View.GONE
            dialogBuilder.setTitle("Select Option")
            dialogBuilder.setPositiveButton("OK") { dialog, whichButton ->
                val selectedOptionId = radioGroup.checkedRadioButtonId
                val selectedOption = dialogView.findViewById<RadioButton>(selectedOptionId)?.text.toString()
                when (selectedOption) {
                    "Update" -> {

                        val dialogBuilder = AlertDialog.Builder(context)
//                        val inflater = this.layoutInflater
                        val dialogView = inflater.inflate(R.layout.popup_window, null)
                        dialogBuilder.setView(dialogView)

                        val editText = dialogView.findViewById<EditText>(R.id.editText)

                        dialogBuilder.setTitle("Edit Item")
                        dialogBuilder.setPositiveButton("Save") { dialog, whichButton ->
                            val newText = editText.text.toString()
                            // Handle the updated text as needed
                            // For example, update the data in the database using dbHelper

//                            var newRD  = ResponseData("topic",newText)

                            val topic = SET_CONFIG
                            awsConfig!!.publishData(newText, topic)



//                            DatabaseHelper(this).updateResponseData(MainActivity.responseAdapter.getString(itemId.toInt()).toString(),newRD)
//                            Toast.makeText(this, "Update option selected", Toast.LENGTH_SHORT).show()

                        }
                        dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
                            // Do nothing or perform any desired action on cancel
                        }

                        val alertDialog = dialogBuilder.create()
                        alertDialog.show()

                        true

                    }
                }
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
                // Do nothing or perform any desired action on cancel
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            dismissDialog()
        }

        button2.setOnClickListener {
            // Handle button 2 click
            // Publish the scanned data to the MQTT topic
            val topic = SET_CONFIG // Replace with your desired MQTT topic
            awsConfig!!.publishData(scannedData, topic)
            dismissDialog()
        }

            button3.setOnClickListener{
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
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button1.isClickable = true
        button2.isClickable = true
//        scannedData = "Name : " +deviceName + "\n" + "Address : " +deviceAddress
        scannedData = deviceAddress
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

    fun buildTurnOffAlertPopup(context: Context, listener: ThreeButtonsListener) : BottomSheetDialog {
        val mBottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))

        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)
       yesButton.setOnClickListener {
            mBottomSheetDialog.dismiss()
            listener.onOkButtonClicked()
        }
        cancelButton.setOnClickListener {
            mBottomSheetDialog.dismiss()
            listener.onCancelButtonClicked()
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.setCancelable(false)
        return mBottomSheetDialog
    }
}
