package com.example.bthome.fragments

import Interfaces.PageChangeListener
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
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
    var pageChangeListener: PageChangeListener? =null


    lateinit var infoTextView : TextView
    lateinit var titleTextView : TextView
    lateinit var introduction : TextView
    lateinit var videoView: LottieAnimationView
    lateinit var okbutton : Button
    lateinit var skipbutton : Button
    lateinit var infoLayout : ConstraintLayout

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
         videoView = rootView.findViewById<LottieAnimationView>(R.id.image)
        okbutton = rootView.findViewById<Button>(R.id.okButton)
        skipbutton = rootView.findViewById<Button>(R.id.cancelButton)
        infoLayout = rootView.findViewById<ConstraintLayout>(R.id.infoLayout)

//        infoTextView.text = "Info for $pageTitle"
//       button.setOnClickListener {
//           findNavController().navigate(R.id.action_informationFragment_to_searchLocationFragment)
//       }

//        okbutton.setOnClickListener {
//            pageChangeListener?.onNextClicked()
//        }

        skipbutton.setOnClickListener {
//            pageTitle ="Shape the Magic Your Way"
            pageChangeListener?.onSkipClicked()
        }
        return rootView
    }


    override fun onResume() {
        super.onResume()
        when(pageTitle){

            "Welcome"->{
                okbutton.visibility= View.GONE
                titleTextView.text = pageTitle
                videoView.setAnimation(R.raw.ftip)
                introduction.visibility= View.GONE
                infoLayout.setBackgroundColor(requireContext().getColor(R.color.light_water_))
               videoView.playAnimation()
                infoTextView.text = "Experience seamless living with our smart home application, bringing automation and convenience to your fingertips."

            }
            "Introduction"->{
            okbutton.visibility= View.GONE
            infoTextView.visibility = View.GONE
            titleTextView.visibility = View.GONE

                infoLayout.setBackgroundColor(requireContext().getColor(R.color.back_active_color))

            videoView.visibility = View.GONE
            introduction.text = "  Get ready to step into your Smart Home\n\n"+
                    "   1. Set Up Your Magical Zone\n" + "   (Pick a spot for your magic touch)\n\n" +
                    "   2. Witness the True Magic\n" + "   (When you're nearby, like magic, lights\n   and fans can turn on or off automatically!)\n\n"+
                    "   3. Shape the Magic Your Way\n" + "   (Make the magic your own! You can\n   customize settings, like having the   fan\n   and lights just the way you want them.)"
                introduction.visibility= View.VISIBLE
        }

            "Set Up Your Magical Zone"->{
                videoView.setAnimation(R.raw.magiczone)
                infoLayout.setBackgroundColor(requireContext().getColor(R.color.light_water_))
                okbutton.visibility= View.GONE
                introduction.visibility= View.GONE
            titleTextView.text = pageTitle
                videoView.playAnimation()
            infoTextView.text = "Step 1: click on the Start.\n" +
                    "Step 2: once you get the location click on the Next.\n" +
                    "Step 3: Choose your location(optional).\n" +
                    "Step 4: Click on the Next."

            }
            "Witness the True Magic"->{
                infoLayout.setBackgroundColor(requireContext().getColor(R.color.light_water_))
            okbutton.visibility= View.GONE
            titleTextView.text = pageTitle
            introduction.visibility= View.GONE

                videoView.playAnimation()
            infoTextView.text = "The app will do the work!\n" +
                    "When you get close to your Location, like Kitchen, the app will see you're almost there.\n" +
                    "Like magic, your fan or light will turn on by itself!\n" +
                    "When you leave that spot, the app knows, and it will turn off your fan or light."

        }
            "Shape the Magic Your Way"->{
                videoView.setAnimation(R.raw.shapemagic)
                infoLayout.setBackgroundColor(requireContext().getColor(R.color.light_water_))
           skipbutton.visibility = View.GONE
            introduction.visibility= View.GONE
            titleTextView.text = pageTitle
                videoView.playAnimation()
            infoTextView.text = "You can set light and fan status how you want once you are near to location, Ex: If you want only light should turn on then you can set light as preference.\n" +
                    "You are  one step behind to set your Smart home, Let's do it now, by clicking on Start.\n"

            okbutton.setOnClickListener {
                findNavController().navigate(R.id.action_informationFragment_to_searchLocationFragment)
            }

        }
        }

    }
}

