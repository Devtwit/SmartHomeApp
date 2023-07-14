package com.example.bthome.fragments

import Adapter.RoomAdapter
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_ble_scan_result, container, false)

        recyclerView = view.findViewById(R.id.scanResultRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemList = listOf("Item 3", "Item 2", "Item 3", "Item 4", "Item 5","Item 6")
        adapter = RoomAdapter(itemList)
        recyclerView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            BleScanResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}