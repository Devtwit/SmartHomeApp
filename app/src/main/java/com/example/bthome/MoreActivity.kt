package com.example.bthome


import Adapter.ItemClickListener
import Adapter.MoreScreenAdapter
import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import Database.DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.GridView

class MoreActivity : AppCompatActivity(), ItemClickListener {

    var awsConfig: AwsConfigClass? = null

companion object{
    lateinit var responseAdapter : MoreScreenAdapter
}
    //    grid layout
    private lateinit var gridView: GridView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(this)


        gridView = findViewById(R.id.ghost_view)

        val dbHelper = DatabaseHelper(this)
        // Initialize the adapter
        responseAdapter = MoreScreenAdapter(emptyList(),this, awsConfig!! )
        gridView.adapter = responseAdapter
        val initialData = dbHelper.getAllResponseData()
        responseAdapter.updateData(initialData)
        setupGridView()
    }
    fun setupGridView() {
        val dbHelper = DatabaseHelper(this)
        val responseDataList = dbHelper.getAllResponseData()
        responseAdapter = MoreScreenAdapter(responseDataList, this, awsConfig!!)
//        responseAdapter = ResponseAdapter(responseDataList,this)
        gridView.adapter = responseAdapter

    }
    override fun onItemClick(itemId: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(itemId: Long) {
        TODO("Not yet implemented")
    }
}