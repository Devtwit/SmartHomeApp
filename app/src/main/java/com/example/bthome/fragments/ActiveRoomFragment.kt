package com.example.bthome.fragments

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigClass
import DatabaseHelper
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bthome.R
import com.example.bthome.databinding.FragmentActiveRoomBinding
import com.example.bthome.databinding.FragmentBleScanResultBinding
import com.example.bthome.viewModels.ActiveRoomViewModel
import com.example.bthome.viewModels.BleScanResultViewModel

class ActiveRoomFragment : Fragment() {
    private lateinit var binding: FragmentActiveRoomBinding
    private lateinit var viewModel: ActiveRoomViewModel
    private lateinit var adapter: RoomAdapter
    var awsConfig: AwsConfigClass? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_active_room, container, false)
        initialize()
        updateDataBase()

        return binding.root

    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ActiveRoomViewModel::class.java
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
        Log.d(TAG, "on Resume ${MoreFragment.idValue.toInt()}")
        binding.scanResultRecyclerView.scrollToPosition(MoreFragment.idValue.toInt())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on Resume")
        updateDataBase()
    }
    companion object{
        private val TAG = ActiveRoomFragment::class.java.simpleName
    }
}