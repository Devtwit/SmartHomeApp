package com.example.bthome.fragments

import Adapter.RoomAdapter
import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bthome.R


class BleScanResultFragment : Fragment() {

    companion object {
        fun newInstance() = BleScanResultFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    var awsConfig: AwsConfigClass? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_ble_scan_result, container, false)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        recyclerView = view.findViewById(R.id.scanResultRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d("Responce", "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        recyclerView.adapter = adapter
        return  view
    }


    override fun onResume() {
        super.onResume()
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        Log.d("Responce on Resume ", "${responseDataList}")
        adapter = RoomAdapter(responseDataList, awsConfig!!)
        recyclerView.adapter = adapter
    }

}