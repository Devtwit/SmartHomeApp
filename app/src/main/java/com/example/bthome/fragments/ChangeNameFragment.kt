package com.example.bthome.fragments
import Data.ResponseData
import Database.DatabaseHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bthome.R

class ChangeNameFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeNameFragment()
    }

//    private lateinit var viewModel: ChangeNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view= inflater.inflate(R.layout.fragment_change_name, container, false)
        var nextButton = view.findViewById<Button>(R.id.next_button)
        var skipButton = view.findViewById<Button>(R.id.skipButton)
        var editText = view.findViewById<EditText>(R.id.device_name_edit_text)


        nextButton.setOnClickListener {
            val newText = editText.text.toString()
            // Handle the updated text as needed
            // For example, update the data in the database using dbHelper

            var newRD  = ResponseData("topic",newText,"")

            DatabaseHelper(requireContext()).updateResponseData(AddBleDeviceFragment.responseAdapter.getString(
                MoreFragment.idValue.toInt()).toString(),newRD)
            Toast.makeText(requireContext(), "Update option selected", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeNameFragment_to_addBleDeviceFragment)
         }

        skipButton.setOnClickListener { findNavController().popBackStack()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ChangeNameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}