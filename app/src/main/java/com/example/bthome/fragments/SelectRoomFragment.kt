package com.example.bthome.fragments

import DatabaseHelper
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.example.bthome.R

class SelectRoomFragment : Fragment() {

    companion object {
        fun newInstance() = SelectRoomFragment()
    }

//    private lateinit var viewModel: SelectRoomViewModel
lateinit var  kitchen: LinearLayout
lateinit var  bedRoom: LinearLayout
lateinit var  poojaRoom: LinearLayout
lateinit var  hall: LinearLayout
lateinit var  store: LinearLayout
lateinit var  study: LinearLayout
    lateinit var nextButton: Button
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view = inflater.inflate(R.layout.fragment_select_room, container, false)

        val skipButton: Button = view.findViewById(R.id.skipButton)
            nextButton = view.findViewById(R.id.next_button)

        kitchen= view.findViewById(R.id.kitchenlayout)
        bedRoom= view.findViewById(R.id.bedroomlayout)
        hall= view.findViewById(R.id.halllayout)
        study= view.findViewById(R.id.studylayout)
        store= view.findViewById(R.id.storeRoom)
        poojaRoom= view.findViewById(R.id.poojaLayout)

        skipButton.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
        }



        return view
    }

    override fun onResume() {
        super.onResume()
        kitchen.setOnClickListener {
            kitchen.isSelected = true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
           kitchenClicked()
        }
        bedRoom.setOnClickListener {
            bedRoom.isSelected = true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
           bedRoomClicked()
        }
        hall.setOnClickListener {
            hall.isSelected = true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
            hallClicked()
        }
        store.setOnClickListener {
            store.isSelected = true
            nextButton.setBackgroundColor(Color.WHITE)
            nextButton.isClickable = true
            storeClicked()
        }
        study.setOnClickListener {
            study.isSelected= true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
            studyClicked()
        }
        poojaRoom.setOnClickListener {
            poojaRoom.isSelected = true
            nextButton.isClickable = true
            nextButton.setBackgroundColor(Color.WHITE)
            poojaRoomClicked()
        }


        nextButton.setOnClickListener {
            if(  kitchen.isSelected ||
                store.isSelected ||
                study.isSelected ||
                poojaRoom.isSelected ||
                hall.isSelected ||
                bedRoom.isSelected) {
                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                    .navigate(R.id.action_selectRoomFragment_to_addBleDeviceFragment)
            } else {
            }

        }
    }


    fun kitchenClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "kitchen")

        kitchen.setBackgroundColor(Color.parseColor("#eaf2ee"))
        bedRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        hall.setBackgroundColor(Color.parseColor("#ffffff"))
        store.setBackgroundColor(Color.parseColor("#ffffff"))
        study.setBackgroundColor(Color.parseColor("#ffffff"))
        poojaRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        kitchen.setBackgroundResource(R.drawable.select_device_selected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        hall.setBackgroundResource(R.drawable.select_device_unselected_bg)
        store.setBackgroundResource(R.drawable.select_device_unselected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        study.setBackgroundResource(R.drawable.select_device_unselected_bg)
    }
    fun poojaRoomClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "Pooja Room")
        kitchen.setBackgroundColor(Color.parseColor("#ffffff"))
        bedRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        hall.setBackgroundColor(Color.parseColor("#ffffff"))
        store.setBackgroundColor(Color.parseColor("#ffffff"))
        study.setBackgroundColor(Color.parseColor("#ffffff"))
        poojaRoom.setBackgroundColor(Color.parseColor("#eaf2ee"))
        kitchen.setBackgroundResource(R.drawable.select_device_unselected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        hall.setBackgroundResource(R.drawable.select_device_unselected_bg)
        store.setBackgroundResource(R.drawable.select_device_unselected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_selected_bg)
        study.setBackgroundResource(R.drawable.select_device_unselected_bg)
    }
    fun storeClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "Store Room")
        kitchen.setBackgroundColor(Color.parseColor("#ffffff"))
        bedRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        hall.setBackgroundColor(Color.parseColor("#ffffff"))
        store.setBackgroundColor(Color.parseColor("#eaf2ee"))
        study.setBackgroundColor(Color.parseColor("#ffffff"))
        poojaRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        kitchen.setBackgroundResource(R.drawable.select_device_unselected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        hall.setBackgroundResource(R.drawable.select_device_unselected_bg)
        store.setBackgroundResource(R.drawable.select_device_selected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        study.setBackgroundResource(R.drawable.select_device_unselected_bg)
    }
    fun studyClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "Study")
        kitchen.setBackgroundColor(Color.parseColor("#ffffff"))
        bedRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        hall.setBackgroundColor(Color.parseColor("#ffffff"))
        store.setBackgroundColor(Color.parseColor("#ffffff"))
        study.setBackgroundColor(Color.parseColor("#eaf2ee"))
        poojaRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        kitchen.setBackgroundResource(R.drawable.select_device_unselected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        hall.setBackgroundResource(R.drawable.select_device_unselected_bg)
        store.setBackgroundResource(R.drawable.select_device_unselected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        study.setBackgroundResource(R.drawable.select_device_selected_bg)
    }
    fun bedRoomClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "Bed Room")
        kitchen.setBackgroundColor(Color.parseColor("#ffffff"))
        bedRoom.setBackgroundColor(Color.parseColor("#eaf2ee"))
        hall.setBackgroundColor(Color.parseColor("#ffffff"))
        store.setBackgroundColor(Color.parseColor("#ffffff"))
        study.setBackgroundColor(Color.parseColor("#ffffff"))
        poojaRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        kitchen.setBackgroundResource(R.drawable.select_device_unselected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_selected_bg)
        hall.setBackgroundResource(R.drawable.select_device_unselected_bg)
        store.setBackgroundResource(R.drawable.select_device_unselected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        study.setBackgroundResource(R.drawable.select_device_unselected_bg)
    }
    fun hallClicked(){
        val oldName = SearchLocationFragment.selectedRoom
        DatabaseHelper(requireContext()).updateLocationName(oldName, "Hall")
        kitchen.setBackgroundColor(Color.parseColor("#ffffff"))
        bedRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        hall.setBackgroundColor(Color.parseColor("#eaf2ee"))
        store.setBackgroundColor(Color.parseColor("#ffffff"))
        study.setBackgroundColor(Color.parseColor("#ffffff"))
        poojaRoom.setBackgroundColor(Color.parseColor("#ffffff"))
        kitchen.setBackgroundResource(R.drawable.select_device_unselected_bg)
        bedRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        hall.setBackgroundResource(R.drawable.select_device_selected_bg)
        store.setBackgroundResource(R.drawable.select_device_unselected_bg)
        poojaRoom.setBackgroundResource(R.drawable.select_device_unselected_bg)
        study.setBackgroundResource(R.drawable.select_device_unselected_bg)
    }
}