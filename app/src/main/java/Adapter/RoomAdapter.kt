package Adapter

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

class RoomAdapter(private val itemList: List<ResponseData>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    // Keep track of the positions of the items that have been animated
    private val animatedPositions = HashSet<Int>()

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
            // Light is on, show different animation
            val lightAnimation = AlphaAnimation(0f, 1f)
            lightAnimation.duration = 1000
            lightAnimation.repeatCount = Animation.INFINITE
            lightAnimation.repeatMode = Animation.REVERSE

            holder.menuButton.startAnimation(lightAnimation)
        } else {
            // Light is off, clear animation
            holder.menuButton.clearAnimation()
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

            holder.menuButton2.startAnimation(fanAnimation)
        } else {
            // Fan is off, clear animation
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

                holder.menuButton.startAnimation(lightAnimation)
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

                holder.menuButton2.startAnimation(fanAnimation)
            } else {
                holder.menuButton2.setImageResource(R.drawable.baseline_toys_24)
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
    }
}
