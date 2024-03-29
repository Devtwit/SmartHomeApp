package com.example.bthome.fragments

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigClass
import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bthome.R

import com.example.bthome.viewModels.BleScanResultViewModel


class BleScanResultFragment : Fragment() {

    private lateinit var binding: com.example.bthome.databinding.FragmentBleScanResultBinding
    private lateinit var viewModel: BleScanResultViewModel
    private lateinit var adapter: RoomAdapter
    var awsConfig: AwsConfigClass? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_ble_scan_result, container, false)
        initialize()
        updateDataBase()

        return binding.root

    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            BleScanResultViewModel::class.java
        )
        binding.viewModel = viewModel

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
    }

    private fun updateDataBase() {
        binding.scanResultRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        (binding.scanResultRecyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
//        recyclerView.layoutManager = layoutManager
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d(TAG, "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        binding.scanResultRecyclerView.adapter = adapter
        binding.scanResultRecyclerView.scrollToPosition(MoreFragment.idValue.toInt())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on Resume")
        updateDataBase()
        binding.imageButton.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).popBackStack()
//            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_bleScanResultFragment_to_mainFragment)
        }
        binding.aButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_bleScanResultFragment_to_addBleDeviceFragment)
        }
//            binding.sButton.setOnClickListener {
//                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
//                    .navigate(R.id.action_addBleDeviceFragment_to_searchLocationFragment)
//            }
            binding.mButton.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                    .navigate(R.id.action_bleScanResultFragment_to_moreFragment)
            }
    }
companion object{
    private val TAG = BleScanResultFragment::class.java.simpleName
}
}