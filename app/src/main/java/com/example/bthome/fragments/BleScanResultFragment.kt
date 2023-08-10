package com.example.bthome.fragments

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import DatabaseHelper
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bthome.R
import com.example.bthome.databinding.FragmentBleScanResultBinding
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.example.bthome.viewModels.BleScanResultViewModel


class BleScanResultFragment : Fragment() {

    companion object {
        fun newInstance() = BleScanResultFragment()
    }

    private lateinit var binding: FragmentBleScanResultBinding
    private lateinit var viewModel: BleScanResultViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    var awsConfig: AwsConfigClass? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBleScanResultBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            BleScanResultViewModel::class.java)
        binding.viewModel = viewModel

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
//        recyclerView = view.findViewById(R.id.scanResultRecyclerView)
        binding.scanResultRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d("Responce", "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        binding.scanResultRecyclerView.adapter = adapter
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d("Responce on Resume ", "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        binding.scanResultRecyclerView.adapter = adapter
    }

}