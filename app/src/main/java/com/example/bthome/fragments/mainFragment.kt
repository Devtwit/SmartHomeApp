package com.example.bthome.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.bthome.R
import com.example.bthome.viewModels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class mainFragment : Fragment() {

    companion object {
        fun newInstance() = mainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNavigationView1: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view = inflater.inflate(R.layout.fragment_main, container, false)

        fragmentContainer = view.findViewById(R.id.fragment_container)

        val fragmentA = AddBleDeviceFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentA)
            .commit()
        bottomNavigationView1 = view.findViewById(R.id.bottomNavigationView)

//         Load the initial fragment

        bottomNavigationView1.selectedItemId = R.id.navigation_home
        // Set up bottom navigation item selection listener
        bottomNavigationView1.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val fragmentA = AddBleDeviceFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentA)
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_addBleDeviceFragment)
                    true
                }
                R.id.navigation_add -> {
                    val fragmentB = SearchLocationFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentB)
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_searchLocationFragment)
                    true
                }
                R.id.navigation_more -> {
                    val fragmentC = MoreFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentC)
                        .commit()
//                    findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_mainFragment_to_moreFragment)
                    true
                }
            }
            true
        }
        return view
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