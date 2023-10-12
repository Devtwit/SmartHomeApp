package com.example.bthome

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.SET_CONFIG
import Data.ResponseData
//import Database.DatabaseHelper
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.net.Uri
import android.provider.Settings

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
import com.google.android.material.bottomsheet.BottomSheetDialog

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


    fun buildTurnOffAlertPopup(context: Context, message: String, isAccess :Boolean,listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var textInfo : TextView = sheetView.findViewById(R.id.descriptionView)
        var textTitle : TextView = sheetView.findViewById(R.id.header_title)
        textInfo.text = message
        textTitle.text = "Access Permission"
        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)
        yesButton.text = "Okay"
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
    fun openSystemSettingPopup(context: Context, listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var textInfo : TextView = sheetView.findViewById(R.id.descriptionView)
        var textTitle : TextView = sheetView.findViewById(R.id.header_title)
        textInfo.text = "Permissions are required to use the app. Please enable them in the app settings."
        textTitle.text = "Access Permission"


        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)

        yesButton.text = "Open Settings"
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
    fun buildNotificationPopup(context: Context, listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)

        var title :TextView = sheetView.findViewById(R.id.header_title)
        title.text = "Notification"
        discription.text = "You can turn on/off Notification in App Settings."
//        discription.visibility = View.GONE
        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)
        yesButton.text = "Open App Settings"
//        cancelButton.visibility = View.GONE
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
    fun buildNoActiveRoomPopup(context: Context, listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)
        var title :TextView = sheetView.findViewById(R.id.header_title)
        title.text = "Currently there is No Active Room"
        discription.visibility = View.GONE
        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        var cancelButton :Button = sheetView.findViewById(R.id.cancel_button)
        yesButton.text = "Okay"
        cancelButton.visibility = View.GONE
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
    fun buildTurnOffAlertPopup(context: Context, listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)
        var title :TextView = sheetView.findViewById(R.id.header_title)
        title.text = " Are you sure you want to delete?"
       discription.visibility = View.GONE
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
    fun buildNameChangeAlertPopup(context: Context, listener: ThreeButtonsListener) : Dialog {
        val mBottomSheetDialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val sheetView: View = inflater.inflate(R.layout.are_you_sure_popup, null)
//        val sheetView = AreYouSurePopup.inflate(LayoutInflater.from(context))
        var discription :TextView = sheetView.findViewById(R.id.descriptionView)
        discription.text = "Are you sure you want to change the name & image of your room."
        var yesButton :Button = sheetView.findViewById(R.id.yes_button)
        yesButton.text = "Update"
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
        yesButton.text= "Select"
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
