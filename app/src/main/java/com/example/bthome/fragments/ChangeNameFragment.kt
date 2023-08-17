package com.example.bthome.fragments

import DatabaseHelper
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.R
import com.example.bthome.databinding.FragmentChangeNameBinding
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.responseAdapter
import com.example.bthome.viewModels.ChangeNameViewModel

class ChangeNameFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeNameFragment()
    }

    private lateinit var binding: FragmentChangeNameBinding
    private lateinit var viewModel: ChangeNameViewModel
//    private lateinit var viewModel: ChangeNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_name, container, false)

        initialize()
        setUpListener()

        return binding.root
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ChangeNameViewModel::class.java
        )
        binding.viewModel = viewModel
    }

    private fun setUpListener() {
        binding.nextButton.setOnClickListener {
            val newText = binding.deviceNameEditText.text.toString()
            val oldName = responseAdapter.getString(MoreFragment.idValue.toInt()).toString()
            DatabaseHelper(requireContext()).updateLocationName(oldName, newText)
            Toast.makeText(requireContext(), "Update option selected", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_changeNameFragment_to_addBleDeviceFragment)
        }

        binding.skipButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}