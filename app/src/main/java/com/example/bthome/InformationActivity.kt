package com.example.bthome


import Adapter.RoomAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager

class InformationActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        recyclerView = findViewById(R.id.scanResultRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf("Item 3", "Item 2", "Item 3", "Item 4", "Item 5","Item 6")
        adapter = RoomAdapter(itemList)
        recyclerView.adapter = adapter
    }
}