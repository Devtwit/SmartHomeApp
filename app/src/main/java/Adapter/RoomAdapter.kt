package Adapter

import AwsConfigThing.AwsConfigClass
import Data.ResponseData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.example.bthome.R

class RoomAdapter(private val itemList: List<ResponseData>,val awsConfig: AwsConfigClass) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    // Keep track of the positions of the items that have been animated
    private val animatedPositions = HashSet<Int>()
companion object{
    var isFanclicked = false
    var isLightclicked = false
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]


        // Bind data to views
        holder.textView.text = item.location

        // Determine the status and set appropriate animations
        val lightStatus = item.devices["light"]?.get("status")
        val fanStatus = item.devices["fan"]?.get("status")

        if (lightStatus == "on") {
            holder.menuButton.setImageResource(R.drawable.glowbulb)
            if(isLightclicked) {
                awsConfig.publishDeviceNameLightOn("BT-Beacon_room1")
            }
            // Light is on, show different animation
            val lightAnimation = AlphaAnimation(0f, 1f)
            lightAnimation.duration = 1000
            lightAnimation.repeatCount = Animation.INFINITE
            lightAnimation.repeatMode = Animation.REVERSE

//            holder.menuButton.startAnimation(lightAnimation)
        } else {
            if(isLightclicked) {
                awsConfig.publishDeviceNameLightOff("BT-Beacon_room1")
            }
            // Light is off, clear animation
            holder.menuButton.clearAnimation()
            holder.menuButton.setImageResource(R.drawable.bulb)
        }

        if (fanStatus == "on") {
//            awsConfig.publishDeviceName("BT-Beacon_room1")
            if(isFanclicked) {
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
            // Fan is off, clear animation
            if(isFanclicked) {
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
            // Add a click listener to menuButton1
            menuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Get the corresponding item
                    val item = itemList[position]
                    isFanclicked= false
                    isLightclicked = true
                    // Toggle the status of menuButton1
                    val lightMap = item.devices["light"] as? MutableMap<String, String>
                    val updatedLightStatus = if (lightMap?.get("status") == "on") "off" else "on"
                    lightMap?.put("status", updatedLightStatus)

                    // Notify the adapter that the data has changed to update the UI
                    notifyItemChanged(position)
                }
            }


            menuButton2.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Get the corresponding item
                    val item = itemList[position]
                    isFanclicked= true
                    isLightclicked = false
                    // Toggle the status of menuButton1
                    val lightMap = item.devices["fan"] as? MutableMap<String, String>
                    val updatedLightStatus = if (lightMap?.get("status") == "on") "off" else "on"
                    lightMap?.put("status", updatedLightStatus)



                    // Notify the adapter that the data has changed to update the UI
                    notifyItemChanged(position)
                }
            }
        }
    }

}
