package com.example.bthome.fragments

import Adapter.ItemClickListener
import Adapter.MoreScreenAdapter
import AwsConfigThing.AwsConfigClass

import DatabaseHelper
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.bthome.R
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.databinding.FragmentMoreBinding
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.example.bthome.viewModels.MoreViewModel

class MoreFragment : Fragment(), ItemClickListener {


    private lateinit var binding: FragmentMoreBinding
    private lateinit var viewModel: MoreViewModel
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
        // Data binding is used to inflate the layout and set up the ViewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MoreViewModel::class.java)
        binding.viewModel = viewModel
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())


//        addRoom = view.findViewById(R.id.room_number_id)
//        addImg = view.findViewById(R.id.imgView)
//        gridView = view.findViewById(R.id.ghost_view)

        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = MoreScreenAdapter(emptyList(),this, awsConfig!! )
        binding.ghostView.adapter = responseAdapter
        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
        setupGridView()
        return binding.root
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = MoreScreenAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        binding.ghostView.adapter = responseAdapter

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
        binding.roomNumberId.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
        binding.imgView.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
    }
}