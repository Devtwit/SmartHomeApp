package com.example.bthome.fragments

import Adapter.ResponseAdapter
import AwsConfigThing.AwsConfigClass
import DatabaseHelper
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bthome.R


class SplashScreenFragment : Fragment() {

    private val ANIMATION_DURATION = 2000 // 2 seconds
    companion object{
        lateinit var apkContext: Context
        lateinit var apkActivity: Activity
    }

    var awsConfig: AwsConfigClass? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        val imageView: ImageView =
            view.findViewById<ImageView>(R.id.image) // Replace with the actual ID of your ImageView
        apkContext = requireActivity().applicationContext
        apkActivity= requireActivity()
//
        awsConfig = AwsConfigClass()
        awsConfig!!.startAwsConfigurations(requireContext())
        // Set up the animation
        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = ANIMATION_DURATION.toLong()

        // Start the animation
        imageView.startAnimation(animation)
        val dbHelper = DatabaseHelper(requireContext())
        val initialData = dbHelper.getAllResponseData()
        Handler(Looper.myLooper()!!).postDelayed({
            if(initialData.isEmpty()){
//                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
            } else {
//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
            }
        },2000)

        return  view
    }

}