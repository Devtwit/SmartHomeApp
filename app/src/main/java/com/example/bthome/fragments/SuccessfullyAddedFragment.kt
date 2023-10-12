package com.example.bthome.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bthome.R
import com.example.bthome.viewModels.SuccessfullyAddedViewModel

class SuccessfullyAddedFragment : Fragment() {

    companion object {
        fun newInstance() = SuccessfullyAddedFragment()
    }

    private lateinit var viewModel: SuccessfullyAddedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       val view= inflater.inflate(R.layout.fragment_successfully_added, container, false)
        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SuccessfullyAddedViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            //doSomethingHere()
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_successfullyAddedFragment_to_selectRoomFragment)
        }, 3000)
    }

}