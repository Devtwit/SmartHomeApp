package Adapter

import AwsConfigThing.AwsConfigClass
import AwsConfigThing.AwsConfigConstants.Companion.GET_CONFIG
import Data.ResponseData
import DatabaseHelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.receivedNearestDeviceName

interface ItemClickListener {
    fun onItemClick(itemId: Long)
    fun onItemLongClick(itemId: Long)
}
class ResponseAdapter(private var responseDataList: List<ResponseData>, private val itemClickListener: ItemClickListener, private val awsConfig: AwsConfigClass) : BaseAdapter() {
    var lastNearestDeviceName = ""
    override fun getCount(): Int {
        return responseDataList.size
    }

    override fun getItem(position: Int): Any {
        return responseDataList[position]
    }
     fun getString(position: Int): Any {
         Log.d("GETALL RESPONCE POSITION",responseDataList[position].address)
        return responseDataList[position].address
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val responseData = getItem(position) as ResponseData
        viewHolder.bind(responseData)

        view.setOnClickListener {
            val itemId = getItemId(position)
            itemClickListener.onItemClick(itemId)
        }
        view.setOnLongClickListener {
            val itemId = getItemId(position)
            itemClickListener.onItemLongClick(itemId)
            true
        }




        Log.d("TAG", receivedNearestDeviceName)
        Log.d("TAG", responseData.address)


        viewHolder.cardColor(responseData.location.equals(receivedNearestDeviceName))
        viewHolder.setImage(responseData.location)
        return view
    }
    fun updateData(newData: List<ResponseData>) {
        responseDataList = newData
        notifyDataSetChanged()
    }
    private class ViewHolder(view: View) {
//        private val topicTextView: TextView = view.findViewById(R.id.topicTextView)
        private val messageTextView: TextView = view.findViewById(R.id.room_number_id)
        private val cardView: CardView = view.findViewById(R.id.card)
        private val selectedImg: ImageView = view.findViewById(R.id.selectedImg)


        fun bind(responseData: ResponseData) {
            messageTextView.text = responseData.location

        }

        fun cardColor(isNear : Boolean){
            if(isNear){
                Log.d("isNear", receivedNearestDeviceName )
                Log.d("isNear", isNear.toString())
                cardView.setBackgroundColor(Color.parseColor("#eaf2ee"))
                cardView.setBackgroundResource(R.drawable.select_device_selected_bg)
            } else {
                Log.d("isNear", receivedNearestDeviceName )
                Log.d("isNear", isNear.toString())
                cardView.setBackgroundColor(Color.WHITE)
                cardView.setBackgroundResource(R.drawable.selected_device_orange)
           }
        }


        fun setImage(name : String){
           when(name){
               "Hall" ->{selectedImg.setBackgroundResource(R.drawable.hall)}
               "Store Room" ->{selectedImg.setBackgroundResource(R.drawable.storeroom)}
               "Study" ->{selectedImg.setBackgroundResource(R.drawable.study)}
               "Bed Room" ->{selectedImg.setBackgroundResource(R.drawable.bedroom)}
               "Pooja Room" ->{selectedImg.setBackgroundResource(R.drawable.poojadiya)}
               "BT-Beacon_room1" ->{selectedImg.setBackgroundResource(R.drawable.kitchen)}
               else ->{selectedImg.setBackgroundResource(R.drawable.other)}
           }

        }
    }


}
