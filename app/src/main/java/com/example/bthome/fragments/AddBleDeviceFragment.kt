package com.example.bthome.fragments

//import Database.DatabaseHelper
import Adapter.ItemClickListener
import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import Bluetooth.HandleBluetooth
import Bluetooth.LeScanCallback
import DatabaseHelper
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.PermissionHandler
import com.example.bthome.R
import com.example.bthome.viewModels.AddBleDeviceViewModel


class AddBleDeviceFragment : Fragment(), LeScanCallback.DeviceFound, ItemClickListener {

    var awsConfig: AwsConfigClass? = null
    private lateinit var viewModel: AddBleDeviceViewModel

    //    grid layout
    private lateinit var gridView: GridView
    companion object {
        lateinit var responseAdapter: ResponseAdapter
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
        Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_addBleDeviceFragment_to_dataBaseUpdateFragment)
    }

     override fun onResume() {
        super.onResume()
        Log.d("Main activity", "on resume")
        blueTooth()
        setupGridView()
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = ResponseAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
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
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE,
    )

    override fun scanCompleted(mScanResult: ArrayList<ScanResult>?, result: ScanResult?) {
//        TODO("Not yet implemented")
        setupGridView()
        processedScanResultIndex = viewModel.precessScanResult(mScanResult!!, result!!,awsConfig,requireContext())
        Log.d("POSITION ", "" + processedScanResultIndex)

    }

}
