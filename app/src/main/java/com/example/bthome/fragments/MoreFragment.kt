package com.example.bthome.fragments

import Adapter.ItemClickListener
import Adapter.MoreScreenAdapter
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
import com.example.bthome.R
import com.example.bthome.databinding.FragmentMoreBinding
import com.example.bthome.viewModels.MoreViewModel

class MoreFragment : Fragment(), ItemClickListener {


    private lateinit var binding: FragmentMoreBinding
    private lateinit var viewModel: MoreViewModel
    var awsConfig: AwsConfigClass? = null

    companion object {
        private val TAG = MoreFragment::class.java.simpleName
        lateinit var responseAdapter: MoreScreenAdapter
        var idValue: Long = 0
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        initialize()
        updateDatabase()
        setupGridView()
        setUpListener()
        return binding.root
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MoreViewModel::class.java
        )
        binding.viewModel = viewModel
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
    }

    private fun updateDatabase() {

        val dbHelper = DatabaseHelper(requireContext())
        // Initialize the adapter
        responseAdapter = MoreScreenAdapter(emptyList(), this, awsConfig!!)
        binding.ghostView.adapter = responseAdapter
        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
    }

    private fun setupGridView() {
        val dbHelper = DatabaseHelper(requireContext())
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = MoreScreenAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        binding.ghostView.adapter = responseAdapter

    }

    private fun setUpListener() {
        binding.roomNumberId.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
        binding.imgView.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_moreFragment_to_searchLocationFragment)
        }
        binding.imageButton.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).popBackStack()
        }
    }

    override fun onItemClick(itemId: Long) {
        idValue = itemId
        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            .navigate(R.id.action_moreFragment_to_dataBaseUpdateFragment)
    }

    override fun onItemLongClick(itemId: Long) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On Resume")
        setUpListener()
    }
}