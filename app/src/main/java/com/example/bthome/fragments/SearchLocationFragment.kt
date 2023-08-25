package com.example.bthome.fragments

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.databinding.FragmentSearchLocationBinding
import com.example.bthome.viewModels.SearchLocationViewModel

class SearchLocationFragment : Fragment(), LeScanCallback.DeviceFound {

    companion object {
        lateinit var scannedResult: String
        lateinit var selectedRoom: String
    }

    private lateinit var binding: FragmentSearchLocationBinding
    private lateinit var viewModel: SearchLocationViewModel

    private var handleBluetooth: HandleBluetooth? = null


    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(requireContext(), requireActivity())
    }

    private val REQUEST_PERMISSION_CODE = 1
    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,

        )

    var awsConfig: AwsConfigClass? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_location, container, false)
        initialize()
        setUpListener()
        return binding.root
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchLocationViewModel::class.java
        )
        binding.viewModel = viewModel

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())

        binding.loadingContainer.visibility = View.GONE
        binding.nextButton.visibility = View.GONE
    }

    private fun setUpListener() {
        binding.skipButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.startButton.setOnClickListener {
            binding.startButton.visibility = View.GONE
            handleBlueTooth()
        }
        binding.nextButton.setOnClickListener {
            binding.loadingContainer.visibility = View.GONE
            // Publish the scanned data to the MQTT topic
            val topic = AwsConfigConstants.SET_CONFIG // Replace with your desired MQTT topic
            Log.d("SearchLocation", "else on button click")
//                awsConfig!!.publishData(scannedResult, topic)
            awsConfig!!.publishDeviceName(scannedResult)

            Handler().postDelayed({
                //doSomethingHere()
                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                    .navigate(R.id.action_searchLocationFragment_to_selectRoomFragment)
            }, 2000)
        }


    }

    private fun handleBlueTooth() {
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {
            if (handleBluetooth == null) {
                handleBluetooth = HandleBluetooth(requireContext(), awsConfig!!)
            }
            binding.loadingContainer.visibility = View.VISIBLE
            handleBluetooth!!.scanLeDevices(this)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.startButton.visibility = View.VISIBLE
        setUpListener()

    }

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
        binding.loadingContainer.visibility = View.GONE
        Log.d("SearchLocation", "scanCompleted")
        if (result != null) {
            Log.d("SearchLocation", "scanCompleted null")
            if (result.device.name == null) {
                binding.foundDevice.text = "BT_ER3C"
                selectedRoom = result.device.address
//                scannedResult = "BT-Beacon_room1 " + result.device.address
                scannedResult = "BT-Beacon_room1"
                Log.d("SearchLocation", "scanCompleted $scannedResult")
            } else {
                binding.foundDevice.text = result.device.name
                selectedRoom = result.device.address
                scannedResult = "BT-Beacon_room1"
//                scannedResult = result.device.name+ " " + result.device.address
//                scannedResult = result.device.name
            }

        }
        Log.d("SearchLocation", "scanCompleted null found")
//        scannedResult= ""
        binding.foundDevice.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
        binding.startButton.visibility = View.GONE
    }

}