package com.example.bthome.fragments

import DatabaseHelper

import android.graphics.Color
import android.os.Bundle

import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.R
import com.example.bthome.databinding.FragmentChangeNameBinding
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.responseAdapter
import com.example.bthome.viewModels.ChangeNameViewModel

class ChangeNameFragment : Fragment() {

    companion object {
        private val TAG = ChangeNameFragment::class.java.simpleName
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On Resume")
        setUpListener()
    }

    private fun setUpListener() {
        var newText = binding.deviceNameEditText.text.toString()
        var oldName = responseAdapter.getString(MoreFragment.idValue.toInt()).toString()

        binding.imageButton.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).popBackStack()
        }
        binding.bed.setOnClickListener {
           newText = "Bed Room"
            binding.bed.setTextColor(Color.BLACK)
            binding.study.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }
        binding.kitchen.setOnClickListener {
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            newText = "Kitchen"
            binding.kitchen.setTextColor(Color.BLACK)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }

        binding.store.setOnClickListener {
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            newText = "Store Room"
            binding.store.setTextColor(Color.BLACK)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }

        binding.study.setOnClickListener {
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            newText = "Study Room"
            binding.study.setTextColor(Color.BLACK)
            binding.bed.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }
        binding.hall.setOnClickListener {
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            newText = "Living Room"
            binding.hall.setTextColor(Color.BLACK)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))

//            binding.deviceNameEditText.isFocusable = false
//            binding.deviceNameEditText.isClickable = false
            binding.deviceNameEditText.text.clear()
//            binding.deviceNameEditText.isFocusableInTouchMode = true

        }
        binding.child.setOnClickListener {
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setTextColor(Color.GRAY)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.BLACK)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            newText = "Child Room"
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }
        binding.car.setOnClickListener {
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setTextColor(Color.GRAY)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.BLACK)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            newText = "Car Parking"
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }
        binding.pooja.setOnClickListener {
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selected_device_orange))
            binding.pooja.setTextColor(Color.BLACK)
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            newText = "Bath Room"
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
//            binding.deviceNameEditText.clearFocus()
            binding.deviceNameEditText.text.clear()
        }

        binding.deviceNameEditText.setOnClickListener{
            binding.study.setTextColor(Color.GRAY)
            binding.bed.setTextColor(Color.GRAY)
            binding.pooja.setTextColor(Color.GRAY)
            binding.hall.setTextColor(Color.GRAY)
            binding.kitchen.setTextColor(Color.GRAY)
            binding.store.setTextColor(Color.GRAY)
            binding.child.setTextColor(Color.GRAY)
            binding.car.setTextColor(Color.GRAY)
            binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
            newText = binding.deviceNameEditText.text.toString()
        }
        binding.nextButton.setOnClickListener {
            if(!binding.deviceNameEditText.text.isEmpty()){
                newText = binding.deviceNameEditText.text.toString()
            }
       if(newText.isNotEmpty()) {
           if(!binding.deviceNameEditText.text.isEmpty()){
               binding.bed.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.kitchen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.hall.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.store.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.study.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.pooja.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               binding.car.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.select_device_unselected_bg))
               newText = binding.deviceNameEditText.text.toString()
               DatabaseHelper(requireContext()).updateLocationName(oldName, newText)
           } else {
               DatabaseHelper(requireContext()).updateLocationName(oldName, newText)
           }
       }
            Toast.makeText(requireContext(), "Update option selected", Toast.LENGTH_SHORT).show()
//            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
//                .navigate(R.id.action_changeNameFragment_to_mainFragment)
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_changeNameFragment_to_addBleDeviceFragment)
        }

//        binding.skipButton.setOnClickListener {
//            findNavController().popBackStack()
//        }
    }
}