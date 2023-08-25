package com.example.bthome.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.navigation.fragment.findNavController
import com.example.bthome.R
import com.example.bthome.R.*


class PageFragment : Fragment() {

    companion object {
        private const val PAGE_TITLE_KEY = "page_title_key"

        fun newInstance(pageTitle: String): PageFragment {
            val args = Bundle()
            args.putString(PAGE_TITLE_KEY, pageTitle)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var pageTitle :String

    lateinit var infoTextView : TextView
    lateinit var titleTextView : TextView
    lateinit var introduction : TextView
    lateinit var videoView: VideoView
    lateinit var button : Button

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layout.fragment_page, container, false)
         pageTitle = arguments?.getString(PAGE_TITLE_KEY) ?: "Default Title"

        infoTextView = rootView.findViewById<TextView>(R.id.infoTextView)
        introduction = rootView.findViewById<TextView>(R.id.introduction)
        titleTextView = rootView.findViewById<TextView>(R.id.title)
         videoView = rootView.findViewById<VideoView>(R.id.image)
        button = rootView.findViewById<Button>(R.id.nextButton)

//        infoTextView.text = "Info for $pageTitle"
       button.setOnClickListener {
           findNavController().navigate(R.id.action_informationFragment_to_searchLocationFragment)
       }
        return rootView
    }


    override fun onResume() {
        super.onResume()
        when(pageTitle){

            "Welcome"->{
                titleTextView.text = pageTitle
                val videoPath = "android.resource://" + requireActivity().packageName + "/" + raw.welcome_smart_home
                val videoUri = Uri.parse(videoPath)
                videoView.setVideoURI(videoUri)
                introduction.visibility= View.GONE
//                val mediaController = MediaController(requireContext())
//                videoView.setMediaController(mediaController)

                videoView.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true  // Set the video to loop
                }
//                // Optional: Set an optional listener for video completion or errors
//                videoView.setOnCompletionListener { mediaPlayer ->
//                    // Do something when video playback completes
//                }
//
//                videoView.setOnErrorListener { mediaPlayer, i, i1 ->
//                    // Handle video playback error
//                    true
//                }
                videoView.start()
                infoTextView.text = "Experience seamless living with our smart home application, bringing automation and convenience to your fingertips."
            }"Introduction"->{
            button.visibility = View.GONE
            infoTextView.visibility = View.GONE
            titleTextView.visibility = View.GONE


            videoView.visibility = View.GONE
            introduction.text = "  Get ready to step into your Smart Home\n\n"+
                    "   1. Set Up Your Magical Zone\n" + "   (Pick a spot for your magic touch)\n\n" +
                    "   2. Witness the True Magic\n" + "   (When you're nearby, like magic, lights\n   and fans can turn on or off automatically!)\n\n"+
                    "   3. Shape the Magic Your Way\n" + "   (Make the magic your own! You can\n   customize settings, like having the   fan\n   and lights just the way you want them.)"
        }

            "Set Up Your Magical Zone"->{
                introduction.visibility= View.GONE
            titleTextView.text = pageTitle
            val videoPath = "android.resource://" + requireActivity().packageName + "/" + raw.onboardinglocation
            val videoUri = Uri.parse(videoPath)
            videoView.setVideoURI(videoUri)
//            val mediaController = MediaController(requireContext())
//            videoView.setMediaController(mediaController)

            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Set the video to loop
            }
            // Optional: Set an optional listener for video completion or errors
//            videoView.setOnCompletionListener { mediaPlayer ->
//                // Do something when video playback completes
//            }
//
//            videoView.setOnErrorListener { mediaPlayer, i, i1 ->
//                // Handle video playback error
//                true
//            }
            videoView.start()
            infoTextView.text = "Step 1: click on the Start.\n" +
                    "Step 2: once you get the location click on the Next.\n" +
                    "Step 3: Choose your location(optional).\n" +
                    "Step 4: Click on the Next."
        }"Witness the True Magic"->{
            titleTextView.text = pageTitle
            introduction.visibility= View.GONE
            val videoPath = "android.resource://" + requireActivity().packageName + "/" + raw.background
            val videoUri = Uri.parse(videoPath)
            videoView.setVideoURI(videoUri)
//            val mediaController = MediaController(requireContext())
//            videoView.setMediaController(mediaController)

            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Set the video to loop
            }
            // Optional: Set an optional listener for video completion or errors
//            videoView.setOnCompletionListener { mediaPlayer ->
//                // Do something when video playback completes
//                videoView.start()
//            }
//
//            videoView.setOnErrorListener { mediaPlayer, i, i1 ->
//                // Handle video playback error
//                true
//            }
            videoView.start()
            infoTextView.text = "The app will do the work!\n" +
                    "When you get close to your Location, like Kitchen, the app will see you're almost there.\n" +
                    "Like magic, your fan or light will turn on by itself!\n" +
                    "When you leave that spot, the app knows, and it will turn off your fan or light."
        }"Shape the Magic Your Way"->{
            button.visibility = View.VISIBLE
            introduction.visibility= View.GONE
            titleTextView.text = pageTitle
            val videoPath = "android.resource://" + requireActivity().packageName + "/" + raw.set_preferance
            val videoUri = Uri.parse(videoPath)
            videoView.setVideoURI(videoUri)
//            val mediaController = MediaController(requireContext())
//            videoView.setMediaController(mediaController)

            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Set the video to loop
            }
            // Optional: Set an optional listener for video completion or errors
//            videoView.setOnCompletionListener { mediaPlayer ->
//                // Do something when video playback completes
//                videoView.start()
//            }
//
//            videoView.setOnErrorListener { mediaPlayer, i, i1 ->
//                // Handle video playback error
//                true
//            }
            videoView.start()
            infoTextView.text = "You can set light and fan status how you want once you are near to location, Ex: If you want only light should turn on then you can set light as preference.\n" +
                    "You are  one step behind to set your Smart home, Let's do it now, by clicking on Start.\n"
        }

        }

    }
}

