package com.example.bthome.fragments

import Adapter.RoomAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bthome.R


class BleScanResultFragment : Fragment() {

    companion object {
        fun newInstance() = BleScanResultFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_ble_scan_result, container, false)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        recyclerView = view.findViewById(R.id.scanResultRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemList = listOf("Item 3", "Item 2", "Item 3", "Item 4", "Item 5","Item 6")
        adapter = RoomAdapter(itemList)
        recyclerView.adapter = adapter
        return  view
    }
}