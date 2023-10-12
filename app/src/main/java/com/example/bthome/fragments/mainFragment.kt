package com.example.bthome.fragments

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.bthome.R
import com.example.bthome.databinding.FragmentAddBleDeviceBinding
import com.example.bthome.databinding.FragmentMainBinding
import com.example.bthome.viewModels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class mainFragment : Fragment() {

    companion object {
        fun newInstance() = mainFragment()
    }
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNavigationView1: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
         binding =    DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContainer = requireView().findViewById(R.id.fragment_container)

        var fragmentA = AddBleDeviceFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragmentA).addToBackStack("AddBleDevice")
            .commit()
//        bottomNavigationView1 = binding.bottomNavigationView

//         Load the initial fragment

        binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        binding.bottomNavigationView.clearViewTranslationCallback()
        // Set up bottom navigation item selection listener
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                     fragmentA = AddBleDeviceFragment()
                    childFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragmentA).addToBackStack("AddBleDevice")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_addBleDeviceFragment)
                    true
                }
                R.id.navigation_add -> {
                    val fragmentB = SearchLocationFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentB).addToBackStack("SearchLocation")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_searchLocationFragment)
                    true
                }
                R.id.navigation_more -> {
                    val fragmentC = MoreFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentC).addToBackStack("More")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_moreFragment)
                    true
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
//        fragmentContainer = requireView().findViewById(R.id.fragment_container)

//        var fragmentA = AddBleDeviceFragment()
//        childFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragmentA)
//            .commit()
        bottomNavigationView1 = binding.bottomNavigationView

//         Load the initial fragment

        binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        binding.bottomNavigationView.callOnClick()
        // Set up bottom navigation item selection listener
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                      var fragmentA = AddBleDeviceFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentA).addToBackStack("AddBleDevice")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_addBleDeviceFragment)
                    true
                }
                R.id.navigation_add -> {
                    val fragmentB = SearchLocationFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentB).addToBackStack("SearchLocation")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_searchLocationFragment)
                    true
                }
                R.id.navigation_more -> {
                    val fragmentC = MoreFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentC).addToBackStack("More")
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_moreFragment)
                    true
                }
            }
            true
        }
    }
//    private fun loadFragment(fragment: Fragment) {
//                val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment_container, fragment)
//        fragmentTransaction.commit()
//    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}