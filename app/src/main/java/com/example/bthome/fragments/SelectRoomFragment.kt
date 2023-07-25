package com.example.bthome.fragments

import DatabaseHelper
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.example.bthome.R

class SelectRoomFragment : Fragment() {

    companion object {
        fun newInstance() = SelectRoomFragment()
    }

    private lateinit var kitchen: LinearLayout
    private lateinit var bedRoom: LinearLayout
    private lateinit var poojaRoom: LinearLayout
    private lateinit var hall: LinearLayout
    private lateinit var store: LinearLayout
    private lateinit var study: LinearLayout
    private lateinit var nextButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_room, container, false)

        val skipButton: Button = view.findViewById(R.id.skipButton)
        nextButton = view.findViewById(R.id.next_button)

        kitchen = view.findViewById(R.id.kitchenlayout)
        bedRoom = view.findViewById(R.id.bedroomlayout)
        hall = view.findViewById(R.id.halllayout)
        study = view.findViewById(R.id.studylayout)
        store = view.findViewById(R.id.storeRoom)
        poojaRoom = view.findViewById(R.id.poojaLayout)

        skipButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
        }

        setupRoomClickListener(kitchen, "BT-Beacon_room1")
        setupRoomClickListener(bedRoom, "Bed Room")
        setupRoomClickListener(hall, "Hall")
        setupRoomClickListener(store, "Store Room")
        setupRoomClickListener(study, "Study")
        setupRoomClickListener(poojaRoom, "Pooja Room")

        nextButton.setOnClickListener {
            val selectedRooms = listOf(
                kitchen.isSelected,
                bedRoom.isSelected,
                hall.isSelected,
                store.isSelected,
                study.isSelected,
                poojaRoom.isSelected
            )

            if (selectedRooms.any { it }) {
                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                    .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
            } else {
                // Handle the case when no room is selected
            }
        }

        return view
    }

    private fun setupRoomClickListener(room: LinearLayout, roomName: String) {
        room.setOnClickListener {
            resetRoomSelection()
            room.isSelected = true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
            updateRoomSelection(room, roomName)
        }
    }

    private fun resetRoomSelection() {
        val rooms = listOf(kitchen, bedRoom, hall, store, study, poojaRoom)
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
