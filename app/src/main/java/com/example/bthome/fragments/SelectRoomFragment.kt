package com.example.bthome.fragments

import DatabaseHelper
import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.R
import com.example.bthome.ThreeButtonsListener
import com.example.bthome.databinding.FragmentSelectRoomBinding
import com.example.bthome.viewModels.SelectRoomViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectRoomFragment : Fragment() {
    private lateinit var binding: FragmentSelectRoomBinding
    private lateinit var viewModel: SelectRoomViewModel

    private var customPopUp: BottomSheetDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_room, container, false)

        initialize()
        setUplistener()
        return binding.root
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SelectRoomViewModel::class.java
        )
        binding.viewModel = viewModel
        customPopUp = BottomSheetDialog(requireContext())
    }

    private fun setUplistener() {
        binding.skipButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
        }

        setupRoomClickListener(binding.kitchenlayout, "Kitchen")
        setupRoomClickListener(binding.bedroomlayout, "Bed Room")
        setupRoomClickListener(binding.halllayout, "Hall")
        setupRoomClickListener(binding.storeRoom, "Store Room")
        setupRoomClickListener(binding.studylayout, "Study Room")
        setupRoomClickListener(binding.poojaLayout, "Pooja Room")

        binding.nextButton.setOnClickListener {
            if(!(binding.kitchenlayout.isSelected ||
            binding.bedroomlayout.isSelected ||
            binding.halllayout.isSelected||
            binding.storeRoom.isSelected ||
            binding.studylayout.isSelected ||
            binding.poojaLayout.isSelected)){
                showErrorPopUp()
            } else {
                showPopUp()
            }
        }
        binding.imageButton.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).popBackStack()
        }

    }
    private fun showErrorPopUp(){

        customPopUp = CustomDialog(requireContext()).buildErrorAlertPopup(requireContext(), object :
            ThreeButtonsListener {
            override fun onOkButtonClicked() {
                val selectedRooms = listOf(
                    binding.kitchenlayout.isSelected,
                    binding.bedroomlayout.isSelected,
                    binding.halllayout.isSelected,
                    binding.storeRoom.isSelected,
                    binding.studylayout.isSelected,
                    binding.poojaLayout.isSelected
                )


                if (selectedRooms.any { it }) {
                    val dbHelper = DatabaseHelper(requireContext())
                    val initialData = dbHelper.getAllResponseData()
                    if (initialData.isEmpty()) {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_informationFragment)
                    } else {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
                    }
                } else {
                    // Handle the case when no room is selected
                }
            }

            override fun onCancelButtonClicked() {
                super.onCancelButtonClicked()
                customPopUp?.dismiss()
            }
        })
        customPopUp?.show()
    }

    private fun showPopUp(){

        customPopUp = CustomDialog(requireContext()).buildNameChangeAlertPopup(requireContext(), object :
            ThreeButtonsListener {
            override fun onOkButtonClicked() {
                val selectedRooms = listOf(
                    binding.kitchenlayout.isSelected,
                    binding.bedroomlayout.isSelected,
                    binding.halllayout.isSelected,
                    binding.storeRoom.isSelected,
                    binding.studylayout.isSelected,
                    binding.poojaLayout.isSelected
                )


                if (selectedRooms.any { it }) {
                    val dbHelper = DatabaseHelper(requireContext())
                    val initialData = dbHelper.getAllResponseData()

                    when{
                        binding.kitchenlayout.isSelected ->{ DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Kitchen")}
                        binding.bedroomlayout.isSelected-> { DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Bed Room")}
                        binding.halllayout.isSelected->{ DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Hall")}
                        binding.storeRoom.isSelected->{ DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Store Room")}
                        binding.studylayout.isSelected->{ DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Study Room")}
                        binding.poojaLayout.isSelected->{ DatabaseHelper(requireContext()).updateLocationName("BT-Beacon_room1", "Pooja Room")}
                    }
                    if (initialData.isEmpty()) {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_informationFragment)
                    } else {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
                    }
                } else {
                    // Handle the case when no room is selected
                }
            }

            override fun onCancelButtonClicked() {
                super.onCancelButtonClicked()
                customPopUp?.dismiss()
            }
        })
        customPopUp?.show()
    }

    private fun setupRoomClickListener(room: LinearLayout, roomName: String) {
        room.setOnClickListener {
            resetRoomSelection()
            room.isSelected = true
            binding.nextButton.isClickable = true
            binding.nextButton.setBackgroundColor(Color.WHITE)
            updateRoomSelection(room, roomName)
        }
    }

    private fun resetRoomSelection() {
        val rooms = listOf(
            binding.kitchenlayout,
            binding.bedroomlayout,
            binding.halllayout,
            binding.storeRoom,
            binding.studylayout,
            binding.poojaLayout
        )
        rooms.forEach { room ->
            room.isSelected = false
            room.setBackgroundColor(Color.WHITE)
            room.setBackgroundResource(R.drawable.select_device_unselected_bg)
        }
    }

    private fun updateRoomSelection(room: LinearLayout, roomName: String) {
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "BT-Beacon_room1")
        room.setBackgroundColor(Color.parseColor("#eaf2ee"))
        room.setBackgroundResource(R.drawable.select_device_selected_bg)
    }
}
