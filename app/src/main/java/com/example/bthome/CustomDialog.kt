package com.example.bthome

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
//import Database.DatabaseHelper
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
     fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
    fun openBackgroundPermission() {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }


    fun buildTurnOffAlertPopup(context: Context, message: String, isAccess :Boolean,listener: ThreeButtonsListener) : BottomSheetDialog {
        val mBottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var textInfo : TextView = sheetView.findViewById(R.id.descriptionView)
        var textTitle : TextView = sheetView.findViewById(R.id.header_title)
        textInfo.text = message
        textTitle.text = "Access Permission"
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
    fun openSystemSettingPopup(context: Context, listener: ThreeButtonsListener) : BottomSheetDialog {
        val mBottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var textInfo : TextView = sheetView.findViewById(R.id.descriptionView)
        var textTitle : TextView = sheetView.findViewById(R.id.header_title)
        textInfo.text = "Permissions are required to use the app. Please enable them in the app settings."
        textTitle.text = "Access Permission"

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
    fun buildNameChangeAlertPopup(context: Context, listener: ThreeButtonsListener) : BottomSheetDialog {
        val mBottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)
        discription.text = "Are you sure you want to change the name of your room."
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
    fun buildErrorAlertPopup(context: Context, listener: ThreeButtonsListener) : BottomSheetDialog {
        val mBottomSheetDialog = BottomSheetDialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))

        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)
        var title :TextView = sheetView.findViewById(R.id.header_title)
        cancelButton.visibility = View.GONE
        discription.text = "Please Select at least one Room."
        title.text = "Select Room"
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
