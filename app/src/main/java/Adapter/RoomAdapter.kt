package Adapter

import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bthome.R
import com.example.bthome.fragments.MoreFragment
import com.example.bthome.fragments.MoreFragment.Companion.idValue

class RoomAdapter(private val itemList: List<ResponseData>,val awsConfig: AwsConfigClass) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    // Keep track of the positions of the items that have been animated
    private val animatedPositions = HashSet<Int>()
    lateinit var  lightMap : MutableMap<String, String>
    lateinit var  fanMap : MutableMap<String, String>


    lateinit var item: ResponseData
companion object{
    var isFanclicked = false
    var isLightclicked = false
    lateinit var hold: ViewHolder
    var  lightStatus :String= ""
    var  fanStatus :String = ""
//    var isFromAcc = false
//    var isFromAccLSA = false
//    var isFromAccFSA = false
    var isFromAccLS = ""
    lateinit var response_Data:ResponseData
    var isFromAccFS = ""
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // Method to update fan and light status based on button clicks
    private fun updateFanLightStatusOnClick(position: Int, fanStatus: String, lightStatus: String) {
        if (position >= 0 && position < itemList.size) {
            val item = itemList[position]
            val fanMap = item.devices["fan"] as? MutableMap<String, String>
            val lightMap = item.devices["light"] as? MutableMap<String, String>

            fanMap?.put("status", fanStatus)
            lightMap?.put("status", lightStatus)
Log.d("ANDRD_DEV UpdateFanLight"," fanMap  ${fanMap} ")
Log.d("ANDRD_DEV UpdateFanLight"," lightMap  ${lightMap} ")
Log.d("ANDRD_DEV UpdateFanLight"," fanStatus  ${fanStatus} ")
Log.d("ANDRD_DEV UpdateFanLight"," lightStatus  ${lightStatus} ")
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUI(dataUI: List<ResponseData>?){

            Log.d("Response dataui room", "$dataUI")
            for (responseData in dataUI!!) {
//                isFromAcc = true
//                isFromAccLSA = true
//                isFromAccFSA = true
//                response_Data=responseData
                // Extract fan and light status
                val fsui = responseData.devices["fan"]?.get("status")
                isFromAccFS = fsui.toString()
                val lsui = responseData.devices["light"]?.get("status")
                fanStatus = fsui.toString()
                lightStatus = lsui.toString()
                isFromAccLS = lsui.toString()
                for (position in 0 until itemList.size) {
                    item = itemList[position]
                    updateFanLightStatusOnClick(position, fanStatus, lightStatus)

                    if (item.address == responseData.address) {
                        if (fsui == "on") {
                            Log.d("Response dataui fan room", "$fsui")
                            val fanAnimation = RotateAnimation(
                                0f,
                                360f,
                                Animation.RELATIVE_TO_SELF,
                                0.5f,
                                Animation.RELATIVE_TO_SELF,
                                0.5f
                            )
                            fanAnimation.duration = 1000
                            fanAnimation.interpolator = LinearInterpolator()
                            fanAnimation.repeatCount = Animation.INFINITE

                            hold.menuButton2.setImageResource(R.drawable.fan_on)
                            hold.menuButton2.setBackgroundResource(R.drawable.gray_background_round)
                            hold.menuButton2.startAnimation(fanAnimation)
                            // Update UI for fan turned on
                            // For example, set a fan icon to indicate fan is on
                        } else {
                            Log.d("Response dataui fan room", "$fsui")
                            // Update UI for fan turned off
                            hold.menuButton2.clearAnimation()
                            hold.menuButton2.setImageResource(R.drawable.baseline_toys_24)
                            hold.menuButton2.setBackgroundResource(R.drawable.white_round_background)
                        }

                        if (lsui == "on") {
                            Log.d("Response dataui light room", "$lsui")
                            hold.menuButton.setImageResource(R.drawable.bulb)
                            hold.menuButton.setBackgroundResource(R.drawable.bulb_glow_background)
                            // Light is on, show different animation
                            val lightAnimation = AlphaAnimation(0f, 1f)
                            lightAnimation.duration = 1000
                            lightAnimation.repeatCount = Animation.INFINITE
                            lightAnimation.repeatMode = Animation.REVERSE
                            // Update UI for light turned on
                            // For example, set a light bulb icon to indicate light is on
                        } else {
                            Log.d("Response dataui light room", "$lsui")
                            // Update UI for light turned off
                            Log.d("Response dataui room", "4")

                            // Light is off, clear animation
                            hold.menuButton.clearAnimation()
                            hold.menuButton.setImageResource(R.drawable.bulb)
                            hold.menuButton.setBackgroundResource(R.drawable.white_background)

                        }
//                       lightMap = (item.devices["light"] as? MutableMap<String, String>)!!
//                        Log.d("Response dataui room", "23 ${lightMap?.get("status")}")
//                        updatedLightStatus = if (lightMap?.get("status") == "on") "off" else "on"
//                        lightMap?.put("status", updatedLightStatus)
//
//
//                        fanMap = (item.devices["fan"] as? MutableMap<String, String>)!!
//                        updatedFanStatus = if (fanMap?.get("status") == "on") "off" else "on"
//                        fanMap?.put("status", updatedFanStatus)

                    }
//                    notifyItemChanged(position)
                    Log.d("ANDRD_DEV UpdateFanLight"," updateUi ")
                }
            }
            notifyDataSetChanged()
        }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        item = itemList[position]
        hold= holder
        if(item.address.equals(itemList[idValue.toInt()].address)) {

            // Bind data to views
            holder.textView.text = item.location
            Log.d("Response dataui room", "11")
            // Determine the status and set appropriate animations
//            val lightStatus = item.devices["light"]?.get("status")
//            val fanStatus = item.devices["fan"]?.get("status")
            val lightStatus = item.devices["light"]?.get("status")
            val fanStatus = item.devices["fan"]?.get("status")

            if (lightStatus == "on") {
//                holder.menuButton.setImageResource(R.drawable.glowbulb)
                if (isLightclicked) {
                    Log.d("Response dataui room", "12")
                    awsConfig.publishDeviceNameLightOn("BT-Beacon_room1")
                }
                Log.d("Response dataui room", "13")
                // Light is on, show different animation
                val lightAnimation = AlphaAnimation(0f, 1f)
                lightAnimation.duration = 1000
                lightAnimation.repeatCount = Animation.INFINITE
                lightAnimation.repeatMode = Animation.REVERSE
                holder.menuButton.setImageResource(R.drawable.bulb)
                holder.menuButton.setBackgroundResource(R.drawable.bulb_glow_background)
//            holder.menuButton.startAnimation(lightAnimation)
            } else {
                Log.d("Response dataui room", "14")
                if (isLightclicked) {
                    Log.d("Response dataui room", "15")
                    awsConfig.publishDeviceNameLightOff("BT-Beacon_room1")
                }
                // Light is off, clear animation
                holder.menuButton.clearAnimation()
                holder.menuButton.setImageResource(R.drawable.bulb)
                holder.menuButton.setBackgroundResource(R.drawable.white_background)
            }

            if (fanStatus == "on") {
                Log.d("Response dataui room", "16")
//            awsConfig.publishDeviceName("BT-Beacon_room1")
                if (isFanclicked) {
                    Log.d("Response dataui room", "17")
                    awsConfig.publishDeviceNameFanOn("BT-Beacon_room1")
                }
                // Fan is on, show different animation
                val fanAnimation = RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                fanAnimation.duration = 1000
                fanAnimation.interpolator = LinearInterpolator()
                fanAnimation.repeatCount = Animation.INFINITE

                holder.menuButton2.startAnimation(fanAnimation)

            } else {
                Log.d("Response dataui room", "17")
                // Fan is off, clear animation
                if (isFanclicked) {
                    Log.d("Response dataui room", "18")
                    awsConfig.publishDeviceNameFanOff("BT-Beacon_room1")
                }
                holder.menuButton2.clearAnimation()
            }

            // Create a fade-in animation
            val fadeInAnimation = AlphaAnimation(0f, 1f)
            fadeInAnimation.duration = 500

            // Apply the fade-in animation to the view
            holder.itemView.startAnimation(fadeInAnimation)

            // Check if the position has been animated before
            if (!animatedPositions.contains(position)) {
                animatedPositions.add(position)
            }
        }
        else{
            holder.itemView.visibility = View.GONE
        }
    }
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        // Restart the animations when a view is attached to the window
        holder.menuButton.clearAnimation()
        holder.menuButton2.clearAnimation()

        if (animatedPositions.contains(holder.adapterPosition)) {
            // Get the corresponding item
            val item = itemList[holder.adapterPosition]

            // Determine the status and set appropriate animations
            val lightStatus = item.devices["light"]?.get("status")
            val fanStatus = item.devices["fan"]?.get("status")

            if (lightStatus == "on") {
                // Light is on, show different animation
                val lightAnimation = AlphaAnimation(0f, 1f)
                lightAnimation.duration = 1000
                lightAnimation.repeatCount = Animation.INFINITE
                lightAnimation.repeatMode = Animation.REVERSE
                holder.menuButton.setImageResource(R.drawable.bulb)
                holder.menuButton.setBackgroundResource(R.drawable.bulb_glow_background)
//                holder.menuButton.startAnimation(lightAnimation)
            } else {
                // Light is off, clear animation
                holder.menuButton.clearAnimation()
                holder.menuButton.setImageResource(R.drawable.bulb)
                holder.menuButton.setBackgroundResource(R.drawable.white_background)
            }

            if (fanStatus == "on") {
                // Fan is on, show different animation
                val fanAnimation = RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                fanAnimation.duration = 1000
                fanAnimation.interpolator = LinearInterpolator()
                fanAnimation.repeatCount = Animation.INFINITE
                holder.menuButton2.setImageResource(R.drawable.fan_on)
                holder.menuButton2.setBackgroundResource(R.drawable.gray_background_round)
                holder.menuButton2.startAnimation(fanAnimation)
            } else {
                // Fan is off, clear animation
                holder.menuButton2.clearAnimation()
                holder.menuButton2.setImageResource(R.drawable.baseline_toys_24)
                holder.menuButton2.setBackgroundResource(R.drawable.white_round_background)
            }
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val menuButton: ImageView = itemView.findViewById(R.id.menuButton)
        val menuButton2: ImageView = itemView.findViewById(R.id.menuButton2)

        // ... other ViewHolder code ...

        init {
            menuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    isFanclicked= false
                    isLightclicked = true
                    if (lightStatus == "on"){
                        awsConfig.publishDeviceNameLightOff("BT-Beacon_room1")
                    } else {
                        awsConfig.publishDeviceNameLightOn("BT-Beacon_room1")
                    }
                    Log.d("ANDRD_DEV UpdateFanLight"," menubutton ")
                }
            }


            menuButton2.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    isFanclicked= true
                    isLightclicked = false
                    if (fanStatus == "on"){
                                awsConfig.publishDeviceNameFanOff("BT-Beacon_room1")
                            } else {
                                awsConfig.publishDeviceNameFanOn("BT-Beacon_room1")
                            }

                    Log.d("ANDRD_DEV UpdateFanLight"," menuButton2 ")

                }
            }
        }
    }

}
