package com.example.bthome.fragments

import Adapter.ItemClickListener
import Adapter.MoreScreenAdapter
import AwsConfigThing.AwsConfigClass

import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.bthome.R

class MoreFragment : Fragment(), ItemClickListener {
//
//    companion object {
//        fun newInstance() = MoreFragment()
//    }

//    private lateinit var viewModel: MoreViewModel




    var awsConfig: AwsConfigClass? = null

    companion object{
        lateinit var responseAdapter : MoreScreenAdapter
         var idValue : Long = 0
    }
    //    grid layout
    private lateinit var gridView: GridView
    lateinit var addRoom :TextView
    lateinit var addImg :ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())


        addRoom = view.findViewById(R.id.room_number_id)
        addImg = view.findViewById(R.id.imgView)
        gridView = view.findViewById(R.id.ghost_view)

        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = MoreScreenAdapter(emptyList(),this, awsConfig!! )
        gridView.adapter = responseAdapter
        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
        setupGridView()
        return view
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = MoreScreenAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        gridView.adapter = responseAdapter

    }
    override fun onItemClick(itemId: Long) {
        idValue = itemId
        Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_moreFragment_to_dataBaseUpdateFragment)
    }

    override fun onItemLongClick(itemId: Long) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        addRoom.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
        addImg.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
    }
}