package com.example.bthome.fragments

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
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.system.exitProcess


class AddBleDeviceFragment : Fragment(), LeScanCallback.DeviceFound, ItemClickListener {

    var awsConfig: AwsConfigClass? = null


    private lateinit var binding: FragmentAddBleDeviceBinding
    private lateinit var viewModel: AddBleDeviceViewModel

    // Add this property to your fragment
    private val selectedDevices = mutableListOf<DeviceSelection>()
    private var handleBluetooth: HandleBluetooth? = null
    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(requireContext(), requireActivity())
    }
    private val REQUEST_PERMISSION_CODE = 1
    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
    )

    companion object {
        lateinit var responseAdapter: ResponseAdapter
        var isLightPref: Boolean = false
        var isFanPref: Boolean = false
        var receivedNearestDeviceName = ""
        var processedScanResultIndex = 0
        var publishStatus: String = "status"
        lateinit var apkContext: Context
        private val TAG = AddBleDeviceFragment::class.java.simpleName

        @SuppressLint("StaticFieldLeak")
        lateinit var dialog: CustomDialog

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_ble_device, container, false)
        initialize()
        setUpListener()
        checkPreferedData()
        updateDatabase()
        setupGridView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if(fragmentManager!!.fragments.count() < 2){
//                    exitProcess(0)
                    requireActivity().finish()
                }
                // Handle the back button press here
                // For example, you can perform some action or navigate back
                // to another Fragment or Activity.
                // If you want to navigate back, you can use the Navigation Component or
                // the FragmentManager to pop the Fragment from the back stack.
                true // Return true to indicate that the event was handled.
            } else {
                false // Return false if you want the default back button behavior.
            }
        }
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AddBleDeviceViewModel::class.java
        )
        binding.viewModel = viewModel

        apkContext = activity!!.applicationContext
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        dialog = CustomDialog(requireContext())
    }

    private fun setUpListener() {
        binding.dotbutton.setOnClickListener {
            Log.d(TAG, "Button Clicked")
            viewModel.showLocationDialog(requireContext(),awsConfig)
        }
        Log.d(TAG, "$selectedDevices")
        binding.moreButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_addBleDeviceFragment_to_moreFragment)

        }
        binding.addButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_addBleDeviceFragment_to_searchLocationFragment)
        }
    }

    private fun updateDatabase() {
        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = ResponseAdapter(emptyList(), this, awsConfig!!)
        binding.gridView.adapter = responseAdapter
        // Fetch the initial data from the database and update the adapter
        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
    }

    private fun checkPreferedData() {
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
            Log.d(TAG, "No devices stored.")
        }
    }

    fun blueTooth() {
        Log.d(TAG, "Required Permission : ${permissions.size}")
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {
            if (handleBluetooth == null) {
                handleBluetooth = HandleBluetooth(requireContext(), awsConfig!!)
            }
            handleBluetooth!!.scanLeDevices(this)
        }
    }

    override fun onItemClick(itemId: Long) {
        MoreFragment.idValue = itemId
        // Handle the item click here
        Log.d(TAG, "Clicked item ID: $itemId")
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_addBleDeviceFragment_to_bleScanResultFragment)

    }

    @SuppressLint("MissingInflatedId")
    override fun onItemLongClick(itemId: Long) {
        MoreFragment.idValue = itemId
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_addBleDeviceFragment_to_dataBaseUpdateFragment)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on resume")
        blueTooth()
        setupGridView()
        checkPreferedData()
    }

    fun setupGridView() {
        val dbHelper = DatabaseHelper(activity!!.applicationContext)
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = ResponseAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        Log.d(TAG, "$responseDataList")
        binding.gridView.adapter = responseAdapter

    }

    private fun checkStoredDevices() {
        val sharedPrefs = requireContext().getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("selectedDevices", null)
        if (json != null) {
            val type = object : TypeToken<List<DeviceSelection>>() {}.type
            val storedDevices = gson.fromJson<List<DeviceSelection>>(json, type)
            Log.d(TAG, storedDevices.toString())
        } else {
            Log.d(TAG, "No devices stored.")
        }
    }

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
        setupGridView()
        processedScanResultIndex =
            viewModel.precessScanResult(mScanResult!!, result!!, awsConfig, requireContext())
        Log.d( TAG, "" + processedScanResultIndex)

    }

}
