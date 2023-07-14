package Adapter

import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bthome.MainActivity
import com.example.bthome.R

class MoreScreenAdapter(private var responseDataList: List<ResponseData>, private val itemClickListener: ItemClickListener, private val awsConfig: AwsConfigClass) : BaseAdapter() {
    var lastNearestDeviceName = ""
    override fun getCount(): Int {
        return responseDataList.size
    }

    override fun getItem(position: Int): Any {
        return responseDataList[position]
    }

    fun getString(position: Int): Any {
        return responseDataList[position].message
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateItem(position: Long, newData: ResponseData) {
        // Update the item in the list
        responseDataList.toMutableList().add(position.toInt(), newData)
//        responseDataList[position] = newData
        notifyDataSetChanged()

        // Update the item in the database
//        databaseHelper.updateResponseData(position.toLong(),newData)
    }

    fun deleteItem(position: Long) {
        // Remove the item from the list
        responseDataList.toMutableList().removeAt(position.toInt())
        notifyDataSetChanged()

        // Delete the item from the database
//        databaseHelper.deleteResponseData(position.toLong())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.more_grid_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }


        val responseData = getItem(position) as ResponseData
        viewHolder.bind(responseData)


//        val responseDataPKEY = responseDataList[position]
//        val itemId = responseDataPKEY.itemId


        view.setOnClickListener {
            val itemId = getItemId(position)
            itemClickListener.onItemClick(itemId)
        }
        view.setOnLongClickListener {
            val itemId = getItemId(position)
            itemClickListener.onItemLongClick(itemId)
            true
        }




        Log.d("TAG", MainActivity.receivedNearestDeviceName)
//        Log.d("TAG", responseData.message)


        viewHolder.cardColor(responseData.message.equals(MainActivity.receivedNearestDeviceName))

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


        fun bind(responseData: ResponseData) {
//            topicTextView.text = responseData.topic
            messageTextView.text = responseData.message

        }

        fun cardColor(isNear: Boolean) {
            if (isNear) {
//                cardView.setBackgroundColor(Color.parseColor("#eaf2ee"))
//                cardView.setBackgroundResource(R.drawable.select_device_selected_bg)
//                cardView.cardElevation = 10.dpToPx()
//
            } else {
//                cardView.setBackgroundColor(Color.WHITE)
//                cardView.setBackgroundResource(R.drawable.select_device_unselected_bg)
//                cardView.cardElevation = 10.dpToPx()
//

            }
        }

        private fun Int.dpToPx(): Float {
            val scale = Resources.getSystem().displayMetrics.density
            return (this * scale + 0.5f)
        }
    }
}