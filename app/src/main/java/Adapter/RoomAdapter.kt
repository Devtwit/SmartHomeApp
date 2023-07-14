package Adapter

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

class RoomAdapter(private val itemList: List<String>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    // Keep track of the positions of the items that have been animated
    private val animatedPositions = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to views
        holder.textView.text = item

        // Create a fade-in animation
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500

        // Apply the fade-in animation to the view
        holder.itemView.startAnimation(fadeInAnimation)

        // Check if the position has been animated before
        if (!animatedPositions.contains(position)) {
            animatedPositions.add(position)

            // Add glowing animation to the first ImageButton
            val glowAnimation = AlphaAnimation(0.5f, 1f)
            glowAnimation.duration = 1000
            glowAnimation.repeatCount = AlphaAnimation.INFINITE
            glowAnimation.repeatMode = AlphaAnimation.REVERSE

            holder.menuButton.startAnimation(glowAnimation)

            // Add fan animation to the second ImageButton
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
            fanAnimation.repeatCount = RotateAnimation.INFINITE

            holder.menuButton2.startAnimation(fanAnimation)
        }
    }



    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        // Restart the animations when a view is attached to the window
        holder.menuButton.clearAnimation()
        holder.menuButton2.clearAnimation()

        if (animatedPositions.contains(holder.adapterPosition)) {
            val glowAnimation = AlphaAnimation(0.5f, 1f)
            glowAnimation.duration = 1000
            glowAnimation.repeatCount = AlphaAnimation.INFINITE
            glowAnimation.repeatMode = AlphaAnimation.REVERSE

            holder.menuButton.startAnimation(glowAnimation)

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
            fanAnimation.repeatCount = RotateAnimation.INFINITE

            holder.menuButton2.startAnimation(fanAnimation)
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