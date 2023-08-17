package com.example.bthome.fragments

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigClass
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
import com.example.bthome.databinding.FragmentBleScanResultBinding
import com.example.bthome.viewModels.BleScanResultViewModel


class BleScanResultFragment : Fragment() {

    private lateinit var binding: FragmentBleScanResultBinding
    private lateinit var viewModel: BleScanResultViewModel
    private lateinit var adapter: RoomAdapter
    var awsConfig: AwsConfigClass? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBleScanResultBinding.inflate(inflater, container, false)
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
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d("Responce", "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        binding.scanResultRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateDataBase()
    }

}