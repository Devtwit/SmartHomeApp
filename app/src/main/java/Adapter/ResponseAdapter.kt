package Adapter

import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import DatabaseHelper

import android.graphics.Bitmap
import android.graphics.Color

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import com.example.bthome.R
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.apkContext
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
            if (dbHelper.getImageByAddress(responseData.address).toString().equals(null)) {
                viewHolder.setImage(responseData.location)
            } else {
                viewHolder.setImageBitMap(
                    responseData.location,
                    dbHelper.getImageByAddress(responseData.address)!!
                )
            }
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
//        private val selectedImgBlack: ImageView = view.findViewById(R.id.selectedImgBack)


        fun bind(responseData: ResponseData) {
            messageTextView.text = responseData.location
            addressTextView.text = responseData.address

        }

        fun cardColor(isNear : Boolean){
            if(isNear){
                Log.d("isNear", receivedNearestDeviceName )
                Log.d("isNear", isNear.toString())
//                cardView.setBackgroundColor(Color.parseColor("#eaf2ee"))
//                cardView.setCardBackgroundColor(Color.parseColor("#424358")) // Set your desired background color here
                cardView.setCardBackgroundColor(apkContext.getColor(R.color.light_water_)) // Set your desired background color here
//                cardView.setCardBackgroundColor(Color.parseColor("#424000")) // Set your desired background color here

//                selectedImg.visibility = View.VISIBLE
//                selectedImgBlack.visibility = View.GONE
//                cardView.setBackgroundResource(R.drawable.select_device_selected_bg)
            } else {
                Log.d("isNear", receivedNearestDeviceName )
                Log.d("isNear", isNear.toString())
//                cardView.setBackgroundColor(Color.parseColor("#424358"))
//                cardView.setCardBackgroundColor(Color.parseColor("#424358"))
//                cardView.setCardBackgroundColor(Color.parseColor("#B5BCDA"))
                cardView.setCardBackgroundColor(apkContext.getColor(R.color.back_active_color))
//                cardView.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
//                selectedImg.visibility = View.GONE
//                selectedImgBlack.visibility = View.VISIBLE

                messageTextView.setTextColor(Color.parseColor("#FF00083A"))
////                val circleImageView = findViewById<CircleImageView>(R.id.selectedImg)
//                val borderColor = ContextCompat.getColor(apkContext, R.color.black) // Change R.color.new_color to the desired color resource
//
//// Create a drawable with a border
//                val drawable = GradientDrawable()
//                drawable.shape = GradientDrawable.OVAL
//                drawable.setStroke(2.dpToPx(), borderColor) // You can change the border width as needed
//
//// Set the custom drawable as the background
//                selectedImg.background = drawable

           }
        }
        fun Int.dpToPx(): Int {
            val scale = apkContext.resources.displayMetrics.density
            return (this * scale + 0.5f).toInt()
        }



        fun setImage(name : String){
           when(name){
               "Hall" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.mono_hall_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.balcony_back)
               }
               "Store Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.mono_storage_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_cleaning_services_24)
               }
               "Study Room" ->{
                   messageTextView.text= name
//                   selectedImg.setBackgroundResource(R.drawable.study)}
                   selectedImg.setBackgroundResource(R.drawable.mono_child_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_local_library_24)
               }
               "Bed Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.mono_bed_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_single_bed)
               }
               "Pooja Room" ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.mono_office_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_diya)
               }
               "Kitchen" ->{
                   messageTextView.text= "Kitchen"
                   selectedImg.setBackgroundResource(R.drawable.mono_catering_back1_logo)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_soup_kitchen)
               }
               else ->{
                   messageTextView.text= name
                   selectedImg.setBackgroundResource(R.drawable.mono_bath_logo1)
//                   selectedImgBlack.setBackgroundResource(R.drawable.outline_deck)
               }
           }

        }
        fun setImageBitMap(name: String,locationImg: Bitmap){
            when(name){
                "Hall" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }
                "Store Room" -> {
                    messageTextView.text = name
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)

                }
                "Study Room" ->{
                    messageTextView.text= name
//                   selectedImg.setBackgroundResource(R.drawable.study)}
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }

                "Bed Room" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }
                "Pooja Room" ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }
                "Kitchen" ->{
                    messageTextView.text= "Kitchen"
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }
                else ->{
                    messageTextView.text= name
                    selectedImg.setImageBitmap(locationImg)
//                    selectedImgBlack.setImageBitmap(locationImg)
                }
            }

        }
    }


}
