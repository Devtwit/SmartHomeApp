package com.example.bthome.fragments


import Data.ResponseData
import Database.DatabaseHelper
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.R
import com.example.bthome.ThreeButtonsListener
import com.example.bthome.fragments.MoreFragment.Companion.idValue

class DataBaseUpdateFragment : Fragment() {

    companion object {
        fun newInstance() = DataBaseUpdateFragment()
    }
    private var customPopUp: BottomSheetDialog? = null
//    private lateinit var viewModel: DataBaseUpdateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
      val  view= inflater.inflate(R.layout.fragment_data_base_update, container, false)
   Log.d("item clicked",idValue.toString())
        var updateClicked = view.findViewById<LinearLayout>(R.id.layout_bg)
        var deletClicked = view.findViewById<LinearLayout>(R.id.layout)
        customPopUp = BottomSheetDialog(requireContext())
        updateClicked.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_dataBaseUpdateFragment_to_changeNameFragment)

//            val dialogBuilder = AlertDialog.Builder(requireContext())
//            val inflater = this.layoutInflater
//            val dialogView = inflater.inflate(R.layout.popup_window, null)
//            dialogBuilder.setView(dialogView)
//
//            val editText = dialogView.findViewById<EditText>(R.id.editText)
//
//            dialogBuilder.setTitle("Edit Item")
//            dialogBuilder.setPositiveButton("Save") { dialog, whichButton ->
//                val newText = editText.text.toString()
//                // Handle the updated text as needed
//                // For example, update the data in the database using dbHelper
//
//                var newRD  = ResponseData("topic",newText)
//
//                DatabaseHelper(requireContext()).updateResponseData(AddBleDeviceFragment.responseAdapter.getString(
//                    idValue.toInt()).toString(),newRD)
//                Toast.makeText(requireContext(), "Update option selected", Toast.LENGTH_SHORT).show()
//                Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_dataBaseUpdateFragment_to_changeNameFragment)
//            }
//            dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
//                // Do nothing or perform any desired action on cancel
//            }
//
//            val alertDialog = dialogBuilder.create()
//            alertDialog.show()


        }

        deletClicked.setOnClickListener{
            Log.d(
                "MainActivity",
                "ON DELETE + ${AddBleDeviceFragment.responseAdapter.getString(idValue.toInt()).toString()}"
            )

            Toast.makeText(requireContext(), "Delete option selected", Toast.LENGTH_SHORT).show()

            customPopUp = CustomDialog(requireContext()).buildTurnOffAlertPopup(requireContext(), object :
                ThreeButtonsListener {
                override fun onOkButtonClicked() {
                    DatabaseHelper(requireContext()).deleteResponseData(AddBleDeviceFragment.responseAdapter.getString(
                idValue.toInt()).toString())
                    Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_dataBaseUpdateFragment_to_addBleDeviceFragment)
                }

                override fun onCancelButtonClicked() {
                    super.onCancelButtonClicked()
                    customPopUp?.dismiss()
                }
            })
            customPopUp?.show()
//            setupGridView()
        }


   return view
    }

}