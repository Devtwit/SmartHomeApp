package com.example.bthome.fragments

import DatabaseHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
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

        setupRoomClickListener(binding.kitchenLayout, "Kitchen")
        setupRoomClickListener(binding.bedroomlayout, "Bed Room")
        setupRoomClickListener(binding.halllayout, "Hall")
        setupRoomClickListener(binding.storeRoom, "Store Room")
        setupRoomClickListener(binding.studylayout, "Study Room")
        setupRoomClickListener(binding.childLayout, "Pooja Room")
        setupRoomClickListener(binding.carLayout, "Pooja Room")
        setupRoomClickListener(binding.bathLayout, "Pooja Room")

        binding.nextButton.setOnClickListener {
            if(!(binding.kitchenLayout.isSelected ||
            binding.bedroomlayout.isSelected ||
            binding.halllayout.isSelected||
            binding.storeRoom.isSelected ||
            binding.studylayout.isSelected ||
                        binding.childLayout.isSelected||
            binding.carLayout.isSelected||
            binding.bathLayout.isSelected
                    )){
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
                    binding.kitchenLayout.isSelected,
                    binding.bedroomlayout.isSelected,
                    binding.halllayout.isSelected,
                    binding.storeRoom.isSelected,
                    binding.studylayout.isSelected,
                    binding.childLayout.isSelected,
                    binding.carLayout.isSelected,
                    binding.bathLayout.isSelected
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
                    binding.kitchenLayout.isSelected,
                    binding.bedroomlayout.isSelected,
                    binding.halllayout.isSelected,
                    binding.storeRoom.isSelected,
                    binding.studylayout.isSelected,
                    binding.childLayout.isSelected,
                    binding.carLayout.isSelected,
                    binding.bathLayout.isSelected
                )


                if (selectedRooms.any { it }) {
                    val dbHelper = DatabaseHelper(requireContext())
                    val initialData = dbHelper.getAllResponseData()
                    var oldName = AddBleDeviceFragment.responseAdapter.getString(MoreFragment.idValue.toInt()).toString()
                    var imageBitmap : Bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_catering_back1_logo)
                    when{
                        binding.kitchenLayout.isSelected ->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_catering_back1_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Kitchen")}
                        binding.bedroomlayout.isSelected-> {
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_bed_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Bed Room")}
                        binding.halllayout.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_hall_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Hall")}
                        binding.storeRoom.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_storage_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Store Room")}
                        binding.studylayout.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_office_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Study Room")}
                        binding.bathLayout.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_bath_logo1)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Bath Room")}
                        binding.carLayout.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_carparking_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Car Parking")}
                        binding.childLayout.isSelected->{
                            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_child_logo)
                            DatabaseHelper(requireContext()).updateLocationName(oldName, "Child Room")}
                    }
//                    var oldName = AddBleDeviceFragment.responseAdapter.getString(MoreFragment.idValue.toInt()).toString()
                    if(oldName.isEmpty()){
                        DatabaseHelper(requireContext()).updateLocationImage("", imageBitmap!!)
                    } else {
                        DatabaseHelper(requireContext()).updateLocationImage(oldName, imageBitmap!!)
                    }
                    if (initialData.isEmpty()) {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_informationFragment)
                    } else {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_selectRoomFragment_to_mainFragment)
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

    private fun setupRoomClickListener(room: CardView, roomName: String) {
        room.setOnClickListener {
            resetRoomSelection()
            room.isSelected = true
            binding.nextButton.isClickable = true
            binding.nextButton.setBackgroundColor(Color.WHITE)
            updateRoomSelection(room, roomName)
        }
    }

    private fun resetRoomSelection() {
        val rooms: List<CardView> = listOf(
            binding.kitchenLayout,
            binding.bedroomlayout,
            binding.halllayout,
            binding.storeRoom,
            binding.studylayout,
            binding.childLayout,
            binding.carLayout,
            binding.bathLayout
        )
        rooms.forEach { room ->
            room.isSelected = false
//            room.setBackgroundColor(Color.WHITE)
//            room.setBackgroundResource(R.drawable.card_shadow_back)
            room.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
        }
    }

    private fun updateRoomSelection(room: CardView, roomName: String) {
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, roomName)
//        room.setBackgroundColor(Color.parseColor("#eaf2ee"))
//        room.setBackgroundResource(R.drawable.select_device_selected_bg)
        room.setCardBackgroundColor(requireContext().getColor(R.color.back_active_color))
    }
}
