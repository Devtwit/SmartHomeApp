package com.example.bthome.fragments



import DatabaseHelper
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.bthome.CustomDialog
import com.example.bthome.R
import com.example.bthome.ThreeButtonsListener
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.databinding.FragmentDataBaseUpdateBinding
import com.example.bthome.fragments.MoreFragment.Companion.idValue
import com.example.bthome.viewModels.AddBleDeviceViewModel
import com.example.bthome.viewModels.DataBaseUpdateViewModel

class DataBaseUpdateFragment : Fragment() {

    companion object {
        fun newInstance() = DataBaseUpdateFragment()
    }
    private var customPopUp: BottomSheetDialog? = null
    private lateinit var binding: FragmentDataBaseUpdateBinding
    private lateinit var viewModel: DataBaseUpdateViewModel
//    private lateinit var viewModel: DataBaseUpdateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Data binding is used to inflate the layout and set up the ViewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_base_update, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DataBaseUpdateViewModel::class.java)
        binding.viewModel = viewModel
   Log.d("item clicked",idValue.toString())
//        var updateClicked = view.findViewById<LinearLayout>(R.id.layout_bg)
//        var deletClicked = view.findViewById<LinearLayout>(R.id.layout)
        customPopUp = BottomSheetDialog(requireContext())
        binding.layoutBg.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_dataBaseUpdateFragment_to_changeNameFragment)

        }

        binding.layout.setOnClickListener{
            Log.d(
                "MainActivity",
                "ON DELETE + ${AddBleDeviceFragment.responseAdapter.getString(idValue.toInt()).toString()}"
            )

            Toast.makeText(requireContext(), "Delete option selected", Toast.LENGTH_SHORT).show()

            customPopUp = CustomDialog(requireContext()).buildTurnOffAlertPopup(requireContext(), object :
                ThreeButtonsListener {
                override fun onOkButtonClicked() {
                    DatabaseHelper(requireContext()).deleteLocation(
                        AddBleDeviceFragment.responseAdapter.getString(
                            idValue.toInt()
                        ).toString()
                    )
                    val dbHelper = DatabaseHelper(requireContext())
                    val initialData = dbHelper.getAllResponseData()
                    if (initialData.isEmpty()) {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_dataBaseUpdateFragment_to_informationFragment)
                    } else {
                        Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                            .navigate(R.id.action_dataBaseUpdateFragment_to_addBleDeviceFragment)
                    }
                }

                override fun onCancelButtonClicked() {
                    super.onCancelButtonClicked()
                    customPopUp?.dismiss()
                }
            })
            customPopUp?.show()
//            setupGridView()
        }


   return binding.root
    }

}