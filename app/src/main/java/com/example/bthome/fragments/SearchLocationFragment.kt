package com.example.bthome.fragments

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import android.Manifest
import android.annotation.SuppressLint
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.PermissionHandler
import com.example.bthome.R

class SearchLocationFragment : Fragment(), LeScanCallback.DeviceFound {

    companion object {
        lateinit var scannedResult : String
        fun newInstance() = SearchLocationFragment()
    }

//    private lateinit var viewModel: SearchLocationViewModel

    var awsConfig: AwsConfigClass? = null
    lateinit var loader : ProgressBar
    lateinit var foundDevice : TextView
    lateinit var nextButton : Button
    lateinit var skipButton : Button
    lateinit var startButton : Button
//    lateinit var scannedResult : String
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_location, container, false)
         skipButton = view.findViewById(R.id.skipButton)
         nextButton = view.findViewById(R.id.next_button)
         startButton = view.findViewById(R.id.start_button)

         loader  = view.findViewById(R.id.loading_container)
         foundDevice = view.findViewById(R.id.foundDevice)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())

//        nextButton.visibility= View.VISIBLE
        loader.visibility= View.GONE
        nextButton.visibility = View.GONE
//        skipButton.setOnClickListener {
//            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_searchLocationFragment_to_changeNameFragment)
//        }
//        nextButton.setOnClickListener {
//            if(nextButton.text.equals("START")) {
////                loader.visibility = View.VISIBLE
//                nextButton.visibility = View.GONE
//                nextButton.text = "Next"
//                permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
//                permissionHandler.checkStatuses()
//                if (permissionHandler.isAllPermissionsEnabled()) {
//                    if (handleBluetooth == null) {
//                        handleBluetooth = HandleBluetooth()
//                    }
//                    loader.visibility = View.VISIBLE
//                    handleBluetooth!!.scanLeDevices(this)
//                }
//            }
//            else{
//                loader.visibility= View.GONE
//                // Publish the scanned data to the MQTT topic
//                val topic = AwsConfigConstants.SET_CONFIG // Replace with your desired MQTT topic
//                Log.d("SearchLocation","else on button click")
//                awsConfig!!.publishData(scannedResult, topic)
//                Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_searchLocationFragment_to_changeNameFragment)
//            }
//        }
        return view
    }

    override fun onResume() {
        super.onResume()
        startButton.visibility= View.VISIBLE
        skipButton.setOnClickListener {
           findNavController().popBackStack()
        }

        startButton.setOnClickListener {
                startButton.visibility = View.GONE
                permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
                permissionHandler.checkStatuses()
                if (permissionHandler.isAllPermissionsEnabled()) {
                    if (handleBluetooth == null) {
                        handleBluetooth = HandleBluetooth(requireContext())
                    }
                    loader.visibility = View.VISIBLE
                    handleBluetooth!!.scanLeDevices(this)
                }

            nextButton.setOnClickListener {
                loader.visibility = View.GONE
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
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE,
    )

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
        loader.visibility = View.GONE
        Log.d("SearchLocation","scanCompleted")
        if (result != null) {
            Log.d("SearchLocation","scanCompleted null")
            if (result.device.name == null) {
                foundDevice.text = "UnknownDevice"
                scannedResult = "UnknownDevice " + result.device.address
                Log.d("SearchLocation","scanCompleted $scannedResult")
            } else {
                foundDevice.text = result.device.name
//                scannedResult = result.device.name+ " " + result.device.address
                scannedResult = result.device.name
            }

        }
        Log.d("SearchLocation","scanCompleted null found")
//        scannedResult= ""
        foundDevice.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        startButton.visibility = View.GONE
    }

}