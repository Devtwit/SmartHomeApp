package com.example.bthome.fragments

import Adapter.ItemClickListener
import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import Data.DeviceSelection
import DatabaseHelper
import Service.FloatingWidgetService
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.ThreeButtonsListener
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.fragments.LoginFragment.Companion.loginName
import com.example.bthome.fragments.SplashScreenFragment.Companion.apkContext
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.system.exitProcess


class AddBleDeviceFragment : Fragment(), LeScanCallback.DeviceFound, ItemClickListener {

    var awsConfig: AwsConfigClass? = null
    private var customPopUp: Dialog? = null

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
//        lateinit var apkContext: Context
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
                if(requireFragmentManager().fragments.count() < 2){
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
        binding.name.text = loginName

//        apkContext = requireActivity().applicationContext
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        dialog = CustomDialog(requireContext())
    }

    private fun setUpListener() {
        customPopUp = Dialog(requireContext())
        binding.fab.setOnClickListener {
            Log.d(TAG, "Button Clicked")
            viewModel.showLocationDialog(requireContext(),awsConfig)
        }
        Log.d(TAG, "$selectedDevices")
//        binding.moreButton.setOnClickListener {
//            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
//                .navigate(R.id.action_addBleDeviceFragment_to_moreFragment)
//
//        }
        binding.addButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_addBleDeviceFragment_to_searchLocationFragment)
        }
        binding.allRoom.setOnClickListener {
            binding.alltextview.text = "All Room"

            binding.button.visibility = View.VISIBLE
            binding.gridView.visibility = View.VISIBLE
            binding.fmContainer.visibility = View.GONE
            setupGridView()
        }

        binding.notification.setOnClickListener {
            showNotificationPopUp()
        }
//        binding.aButton.setOnClickListener {
//            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
//                .navigate(R.id.action_addBleDeviceFragment_to_searchLocationFragment)
//        }
        binding.sButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_addBleDeviceFragment_to_bleScanResultFragment)
        }
        binding.mButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_addBleDeviceFragment_to_moreFragment)
        }




        binding.activeRoom.setOnClickListener {
            if (viewModel.hasDeviceInRange) {
//            MoreFragment.idValue = 4
                binding.alltextview.text = "Active Room"
                binding.gridView.visibility = View.GONE
                binding.button.visibility = View.GONE
                binding.fmContainer.visibility = View.VISIBLE
                val childFragment = ActiveRoomFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.fmContainer, childFragment)
                    .commit()
            Log.d(TAG, "item ID: ${viewModel.hasDeviceInRangeId.toLong()}")
            MoreFragment.idValue = viewModel.hasDeviceInRangeId.toLong()
            } else {
                showPopUp()
            }
        }
    }
    private fun showPopUp(){

        customPopUp = CustomDialog(requireContext()).buildNoActiveRoomPopup(requireContext(), object :
            ThreeButtonsListener {
            override fun onOkButtonClicked() {
                super.onOkButtonClicked()
                customPopUp?.dismiss()
            }


        })
        customPopUp?.show()
    }
    private fun showNotificationPopUp(){

        customPopUp = CustomDialog(requireContext()).buildNotificationPopup(requireContext(), object :
            ThreeButtonsListener {
            override fun onOkButtonClicked() {
                super.onOkButtonClicked()
                CustomDialog(requireContext()).openAppSettings()
                customPopUp?.dismiss()
            }


        })
        customPopUp?.show()
    }

     fun updateDatabase() {
        val dbHelper = DatabaseHelper(apkContext)
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
//        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
//            .navigate(R.id.action_mainFragment_to_bleScanResultFragment)
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_addBleDeviceFragment_to_bleScanResultFragment)

    }

    @SuppressLint("MissingInflatedId")
    override fun onItemLongClick(itemId: Long) {
        MoreFragment.idValue = itemId
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_addBleDeviceFragment_to_dataBaseUpdateFragment)
    }
    private val overlayPermissionRequestCode = 101 // You can choose any request code
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on resume")
        blueTooth()
        setupGridView()
        checkPreferedData()
        setUpListener()


// Check if the permission is granted
//        if (!Settings.canDrawOverlays(requireContext())) {
//            // Permission is not granted, request it
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
//            intent.data = Uri.parse("package:${requireActivity().packageName}")
//            startActivityForResult(intent, overlayPermissionRequestCode)
//        } else {
//            // Permission is already granted, initialize the floating widget
////            startFloatingWidgetService()
//        }
//        startFloatingWidgetService()

    }
    private fun startFloatingWidgetService() {
        val intent = Intent(requireContext(), FloatingWidgetService::class.java)
        requireActivity().startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == overlayPermissionRequestCode) {
            if (Settings.canDrawOverlays(requireContext())) {
                // Permission has been granted, initialize the floating widget
//                startFloatingWidgetService()
            } else {
                // Permission was not granted by the user
                // You can show a message or take appropriate action here
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun setupGridView() {
        if(isAdded || isVisible) {
            val dbHelper = DatabaseHelper(requireActivity().applicationContext)
            val responseDataList = dbHelper.getAllResponseData()
            responseAdapter = ResponseAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
            Log.d(TAG, "$responseDataList")
            binding.gridView.adapter = responseAdapter
        }

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
            viewModel.precessScanResult(mScanResult!!, result!!, awsConfig, apkContext)
        Log.d( TAG, "" + processedScanResultIndex)

    }

}
