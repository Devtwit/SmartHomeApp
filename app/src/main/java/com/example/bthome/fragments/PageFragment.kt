package com.example.bthome.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.bthome.R


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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_page, container, false)
        val pageTitle = arguments?.getString(PAGE_TITLE_KEY) ?: "Default Title"

        val infoTextView = rootView.findViewById<TextView>(R.id.infoTextView)
        val button = rootView.findViewById<Button>(R.id.nextButton)
        when(pageTitle){
            "Page 1"->{
                infoTextView.text = "Info for $pageTitle"
            }"Page 2"->{
            infoTextView.text = "Info for $pageTitle"
            }"Page 3"->{
            infoTextView.text = "Info for $pageTitle"
            }"Page 4"->{
            infoTextView.text = "Info for $pageTitle"
            button.visibility = View.VISIBLE
            }
        }
//        infoTextView.text = "Info for $pageTitle"
       button.setOnClickListener {
           findNavController().navigate(R.id.action_informationFragment_to_searchLocationFragment)
       }
        return rootView
    }
}

