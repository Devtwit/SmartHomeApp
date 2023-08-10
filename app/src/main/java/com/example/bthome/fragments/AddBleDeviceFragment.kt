package com.example.bthome.fragments

//import Database.DatabaseHelper
import Adapter.ItemClickListener
import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import Data.DeviceSelection
import DatabaseHelper
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddBleDeviceFragment : Fragment(), LeScanCallback.DeviceFound, ItemClickListener {

    var awsConfig: AwsConfigClass? = null
    private lateinit var viewModel: AddBleDeviceViewModel
    // Add this property to your fragment
    private val selectedDevices = mutableListOf<DeviceSelection>()
    //    grid layout
    private lateinit var gridView: GridView
    companion object {
        lateinit var responseAdapter: ResponseAdapter
         var isLightPref :Boolean = false
         var isFanPref :Boolean = false
        var receivedNearestDeviceName = ""
        var processedScanResultIndex = 0
        var publishStatus : String = "status"
        lateinit var apkContext :Context



        @SuppressLint("StaticFieldLeak")
        lateinit var dialog: CustomDialog
        fun newInstance() = AddBleDeviceFragment()
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_ble_device, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AddBleDeviceViewModel::class.java)
        apkContext=activity!!.applicationContext
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        dialog = CustomDialog(requireContext())

//        val scanButton: ImageButton = view.findViewById(R.id.addBleDeviceBtn)
        val moreButton: Button = view.findViewById(R.id.moreButton)
        val addButton: Button = view.findViewById(R.id.addButton)
        val dotButton: ImageView = view.findViewById(R.id.dotbutton)


        // Check and apply preferences for fan and light devices
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)


        val gson = Gson()
        val json = sharedPrefs.getString("selectedDevices", null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            val storedDevices = gson.fromJson<List<DeviceSelection>>(json, type)
            selectedDevices.clear()
            selectedDevices.addAll(storedDevices)

            // Check the stored devices
            checkStoredDevices()
        } else {
            Log.d("StoredDevices", "No devices stored.")
        }
        dotButton.setOnClickListener {
            Log.d("selectedDevices", "Button Clicked")
            showLocationDialog()
        }
//        dotButton.setOnClickListener {
//            Log.d("selectedDevices", "Button Clicked")
//            showDeviceListDialog()
//            Log.d("selectedDevices", "Button Clicked")
//        }
        Log.d("selectedDevices","$selectedDevices")
        moreButton.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_moreFragment)

        }
        addButton.setOnClickListener {
//            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_selectRoomFragment)
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_searchLocationFragment)
        }
        gridView = view.findViewById(R.id.gridView)

        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = ResponseAdapter(emptyList(),this, awsConfig!! )
        gridView.adapter = responseAdapter

        // Fetch the initial data from the database and update the adapter

        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
//        blueTooth()
        setupGridView()
        return  view
    }
    fun blueTooth(){
        Log.d("Permission", "Required Permission : ${permissions.size}")
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {
            if (handleBluetooth == null) {
                handleBluetooth = HandleBluetooth(requireContext(),awsConfig!!)
            }
            handleBluetooth!!.scanLeDevices(this)
        }
    }
    override fun onItemClick(itemId: Long) {
        // Handle the item click here
        Log.d("MainActivity", "Clicked item ID: $itemId")
        Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_bleScanResultFragment)

    }
    @SuppressLint("MissingInflatedId")
    override fun onItemLongClick(itemId: Long) {
        MoreFragment.idValue = itemId
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_addBleDeviceFragment_to_dataBaseUpdateFragment)
    }
    private fun showLocationDialog() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()

        val locationNames = responseDataList.map { it.location }

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Select a Location")

        dialogBuilder.setItems(locationNames.toTypedArray()) { _, position ->
            val selectedLocation = locationNames[position]

            handleLocationSelection(selectedLocation)
            handleSelectedDevices(selectedDevices, selectedLocation)
            handleSelectedDevices(selectedLocation)
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun handleLocationSelection(selectedLocation: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Select Preferences")

        val deviceNames = arrayOf("Fan", "Light") // List of device names
        val storedDevices = loadStoredDevices(selectedLocation) // Load devices from shared preferences

        val deviceSelections = deviceNames.map { deviceName ->
            DeviceSelection(deviceName, storedDevices.any { it.deviceId == deviceName })
        }

        val initialSelections = deviceSelections.map { it.isSelected }.toBooleanArray()

        dialogBuilder.setMultiChoiceItems(
            deviceNames,
            initialSelections
        ) { _, position, isChecked ->
            deviceSelections[position].isSelected = isChecked
        }

        dialogBuilder.setPositiveButton("OK") { _, _ ->
            // Update the selectedDevices list with the new selections
            selectedDevices.clear()
            selectedDevices.addAll(deviceSelections)

            // Save the selected preferences to persistent storage for the chosen location
            saveSelectedDevices(selectedLocation, selectedDevices)
        }

        dialogBuilder.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun saveSelectedDevices(selectedLocation: String, selectedDevices: List<DeviceSelection>) {
        // Save the selected devices to persistent storage for the chosen location
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val gson = Gson()
        val json = gson.toJson(selectedDevices)
        editor.putString(selectedLocation, json)
        editor.apply()
    }


    private fun loadStoredDevices(selectedLocation: String): List<DeviceSelection> {
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(selectedLocation, null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }


     override fun onResume() {
        super.onResume()
        Log.d("Main activity", "on resume")
        blueTooth()
        setupGridView()
         val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
         val gson = Gson()
         val json = sharedPrefs.getString("selectedDevices", null)
         if (json != null) {
             val type = object : TypeToken<List<DeviceSelection>>() {}.type
             val storedDevices = gson.fromJson<List<DeviceSelection>>(json, type)
             selectedDevices.clear()
             selectedDevices.addAll(storedDevices)

             // Check the stored devices
             checkStoredDevices()
         } else {
             Log.d("StoredDevices", "No devices stored.")
         }
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = ResponseAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        Log.d("responseDataList","$responseDataList")
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
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private fun checkStoredDevices() {
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("selectedDevices", null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            val storedDevices = gson.fromJson<List<DeviceSelection>>(json, type)
            Log.d("StoredDevices", storedDevices.toString())
        } else {
            Log.d("StoredDevices", "No devices stored.")
        }
    }

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
        setupGridView()
        processedScanResultIndex = viewModel.precessScanResult(mScanResult!!, result!!,awsConfig,requireContext())
        Log.d("POSITION ", "" + processedScanResultIndex)

    }

    private fun handleSelectedDevices(selectedLocation: String) {
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
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
                    performBothSelectedAction()
                }
                fanSelected -> {
                    // Only Fan is selected, perform Fan selected action
                    isLightPref = false
                    isFanPref = true
                    performFanSelectedAction()
                }
                lightSelected -> {
                    // Only Light is selected, perform Light selected action
                    isFanPref = false
                    isLightPref = true
                    performLightSelectedAction()
                }
                else -> {
                    isFanPref = false
                    isLightPref = false
                    // None selected or other cases, handle accordingly
                    // For example, display a message or perform a default action
                }
            }
        } else {
            // Handle the case when no preferences are stored for the selected location
        }
    }

    private fun handleSelectedDevices(selectedDevices: List<DeviceSelection>, selectedLocation: String) {
        val fanSelected = selectedDevices.any { it.deviceId == "Fan" && it.isSelected }
        val lightSelected = selectedDevices.any { it.deviceId == "Light" && it.isSelected }

        when {
            fanSelected && lightSelected -> {
                // Both Fan and Light are selected, perform specific action
                isLightPref = true
                isFanPref = true
//                performBothSelectedAction(selectedLocation)
            }
            fanSelected -> {
                // Only Fan is selected, perform Fan selected action
                isLightPref = false
                isFanPref = true
//                performFanSelectedAction(selectedLocation)
            }
            lightSelected -> {
                // Only Light is selected, perform Light selected action
                isFanPref = false
                isLightPref = true
//                performLightSelectedAction(selectedLocation)
            }
            else -> {
                isFanPref = false
                isLightPref = false
                // None selected or other cases, handle accordingly
                // For example, display a message or perform a default action
            }
        }
    }


    private fun performBothSelectedAction() {
        onResume()
        // Do something when both Fan and Light are selected
        // For example:
        Log.d("SelectedDevices", "Both Fan and Light selected")
    }

    private fun performFanSelectedAction() {
        onResume()
        // Do something when only Fan is selected
        // For example:
        Log.d("SelectedDevices", "Only Fan selected")
    }

    private fun performLightSelectedAction() {
        onResume()
        // Do something when only Light is selected
        // For example:
        Log.d("SelectedDevices", "Only Light selected")
    }

}
