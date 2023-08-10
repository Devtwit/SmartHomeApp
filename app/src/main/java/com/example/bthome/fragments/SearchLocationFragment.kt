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
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.databinding.FragmentSearchLocationBinding
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.example.bthome.viewModels.SearchLocationViewModel

class SearchLocationFragment : Fragment(), LeScanCallback.DeviceFound {

    companion object {
        lateinit var scannedResult : String
        lateinit var selectedRoom : String
        fun newInstance() = SearchLocationFragment()
    }

    private lateinit var binding: FragmentSearchLocationBinding
    private lateinit var viewModel: SearchLocationViewModel


    var awsConfig: AwsConfigClass? = null
//    lateinit var loader : ProgressBar
//    lateinit var foundDevice : TextView
//    lateinit var nextButton : Button
//    lateinit var skipButton : Button
//    lateinit var startButton : Button
//    lateinit var scannedResult : String
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
    // Data binding is used to inflate the layout and set up the ViewModel
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_location, container, false)
    viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
        SearchLocationViewModel::class.java)
    binding.viewModel = viewModel
//         skipButton = view.findViewById(R.id.skipButton)
//         nextButton = view.findViewById(R.id.next_button)
//         startButton = view.findViewById(R.id.start_button)
//
//         loader  = view.findViewById(R.id.loading_container)
//         foundDevice = view.findViewById(R.id.foundDevice)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())

//        nextButton.visibility= View.VISIBLE
        binding.loadingContainer.visibility= View.GONE
        binding.nextButton.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.startButton.visibility= View.VISIBLE
        binding.skipButton.setOnClickListener {
           findNavController().popBackStack()
        }

        binding.startButton.setOnClickListener {
            binding.startButton.visibility = View.GONE
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

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
        binding.loadingContainer.visibility = View.GONE
        Log.d("SearchLocation","scanCompleted")
        if (result != null) {
            Log.d("SearchLocation","scanCompleted null")
            if (result.device.name == null) {
                binding.foundDevice.text = "UnknownDevice"
                selectedRoom = result.device.address
//                scannedResult = "BT-Beacon_room1 " + result.device.address
                scannedResult = "BT-Beacon_room1"
                Log.d("SearchLocation","scanCompleted $scannedResult")
            } else {
                binding.foundDevice.text = result.device.name
                selectedRoom = result.device.address
                scannedResult = "BT-Beacon_room1"
//                scannedResult = result.device.name+ " " + result.device.address
//                scannedResult = result.device.name
            }

        }
        Log.d("SearchLocation","scanCompleted null found")
//        scannedResult= ""
        binding.foundDevice.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
        binding.startButton.visibility = View.GONE
    }

}