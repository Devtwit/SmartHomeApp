package Adapter

import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import DatabaseHelper

import android.graphics.Bitmap
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


        viewHolder.cardColor(responseData.address.equals(receivedNearestDeviceName))
        val dbHelper = DatabaseHelper(parent.context)
//        dbHelper.updateLocationImage(responseData.address,dbHelper.getImageByAddress(responseData.address)!!)
        if(!dbHelper.isImageNullOrMissing(responseData.address)) {
//            if (dbHelper.getImageByAddress(responseData.address).toString().equals(null)) {
//                viewHolder.setImage(responseData.location)
//            } else {
                viewHolder.setImageBitMap(
                    responseData.location,
                    dbHelper.getImageByAddress(responseData.address)!!
                )
//            }
        } else{
//            dbHelper.updateLocationImageByAddress(responseData.address,R.drawable.poojadiya)
            viewHolder.setImage(responseData.location)
        }
        return view
    }
    fun updateData(newData: List<ResponseData>) {
        responseDataList = newData
        notifyDataSetChanged()
    }
    private class ViewHolder(view: View) {
//        private val topicTextView: TextView = view.findViewById(R.id.topicTextView)
        private val messageTextView: TextView = view.findViewById(R.id.room_number_id)
        private val addressTextView: TextView = view.findViewById(R.id.address)
        private val cardView: CardView = view.findViewById(R.id.card)
        private val selectedImg: ImageView = view.findViewById(R.id.selectedImg)


        fun bind(responseData: ResponseData) {
            messageTextView.text = responseData.location
            addressTextView.text = responseData.address

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
               "Hall" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.hall)
               }
               "Store Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.storeroom)}
               "Study Room" ->{
                   messageTextView.text= name
//                   selectedImg.setBackgroundResource(R.drawable.study)}
                   selectedImg.setBackgroundResource(R.drawable.study)}
               "Bed Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.bedroom)}
               "Pooja Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.poojadiya)}
               "Kitchen" ->{
                   messageTextView.text= "Kitchen"
                   selectedImg.setBackgroundResource(R.drawable.kitchen)}
               else ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.other)}
           }

        }
        fun setImageBitMap(name: String,locationImg: Bitmap){
            when(name){
                "Hall" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)
                }
                "Store Room" -> {
                    messageTextView.text = name
                    selectedImg.setImageBitmap(locationImg)
                }
                "Study Room" ->{
                    messageTextView.text= name
//                   selectedImg.setBackgroundResource(R.drawable.study)}
                    selectedImg.setImageBitmap(locationImg)}
                "Bed Room" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)}
                "Pooja Room" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)}
                "Kitchen" ->{
                    messageTextView.text= "Kitchen"
                    selectedImg.setImageBitmap(locationImg)}
                else ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)}
            }

        }
    }


}
