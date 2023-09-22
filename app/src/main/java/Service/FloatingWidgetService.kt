package Service

import Adapter.DeviceListAdapter
import AwsConfigThing.AwsConfigClass
import Data.DeviceSelection
import DatabaseHelper
import android.app.AlertDialog
import android.app.Dialog

import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.bthome.R

class FloatingWidgetService : Service(),DeviceListAdapter.ItemClickListener  {
    private var windowManager: WindowManager? = null
    private var floatingWidgetView: View? = null
    var awsConfig: AwsConfigClass? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private val selectedDevices = mutableListOf<DeviceSelection>()
   lateinit var listdialog : Dialog
    override fun onCreate() {
        super.onCreate()

        // Create a floating widget view
        floatingWidgetView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(applicationContext)
        // Configure the layout parameters for the floating widget
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // Set the position of the floating widget (you can adjust this)
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        // Initialize the window manager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Add the floating widget to the window manager
        windowManager?.addView(floatingWidgetView, params)


        // Set a click listener for the floating widget
        val floatingWidgetButton = floatingWidgetView?.findViewById<ImageView>(R.id.floatingWidgetButton)
        val closeButton = floatingWidgetView?.findViewById<ImageView>(R.id.close_button)
        floatingWidgetButton!!.visibility = View.VISIBLE
         listdialog = Dialog(applicationContext)

            floatingWidgetButton?.setOnClickListener {
//             Inflate the popup layout
                val popupView = LayoutInflater.from(applicationContext).inflate(R.layout.device_list_popup, null)
//
//                // Create a dialog to display the popup

                listdialog.setTitle("Select a Location")
            listdialog.setContentView(popupView)
            val dbHelperData = DatabaseHelper(applicationContext)
            val deviceListData = dbHelperData.getAllResponseData()
            // Initialize the RecyclerView and adapter to display the list of devices
                val deviceRecyclerView = popupView.findViewById<RecyclerView>(R.id.deviceRecyclerView)
                val deviceListAdapter = DeviceListAdapter(deviceListData,this) // Create your adapter here
                deviceRecyclerView.adapter = deviceListAdapter

                // Set a layout manager for the RecyclerView (e.g., LinearLayoutManager)
                deviceRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

                // Show the dialog
            if (Settings.canDrawOverlays(this)) {
//                val alertDialog = dialog.create()
                listdialog.getWindow()?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                listdialog.show()
            } else {
                Toast.makeText(applicationContext, "No overlay permission Clicked", Toast.LENGTH_SHORT).show()
                // You don't have permission, you can request it or handle it accordingly
            }

        }
        closeButton?.setOnClickListener {
            // Open a popup or perform any action here
            stopSelf()
            Toast.makeText(this, "close Clicked", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onItemClick(device: Long) {
        // Handle item click here
        // Close the list dialog
        listdialog.dismiss()

        // Perform any other actions you need to do when an item is clicked
    }
    override fun onDestroy() {
        super.onDestroy()

        // Remove the floating widget from the window manager
        windowManager?.removeView(floatingWidgetView)
    }


}
