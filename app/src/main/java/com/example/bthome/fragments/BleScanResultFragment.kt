package com.example.bthome.fragments

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bthome.R
import com.example.bthome.viewModels.BleScanResultViewModel

class BleScanResultFragment : Fragment() {

    companion object {
        fun newInstance() = BleScanResultFragment()
    }

    private lateinit var viewModel: BleScanResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ble_scan_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            BleScanResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}