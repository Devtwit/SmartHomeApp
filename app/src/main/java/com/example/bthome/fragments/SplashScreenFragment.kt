package com.example.bthome.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.bthome.R


class SplashScreenFragment : Fragment() {

    private val ANIMATION_DURATION = 2000 // 2 seconds


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        val imageView: ImageView =
            view.findViewById<ImageView>(R.id.image) // Replace with the actual ID of your ImageView

//

        // Set up the animation
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = ANIMATION_DURATION.toLong()

        // Start the animation
        imageView.startAnimation(animation)

        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
        },2000)

        return  view
    }

}