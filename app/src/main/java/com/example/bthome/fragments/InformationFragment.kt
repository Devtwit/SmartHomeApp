package com.example.bthome.fragments


import Interfaces.PageChangeListener
import android.annotation.SuppressLint
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.example.bthome.R
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator


class InformationFragment : Fragment(), PageChangeListener {

    private lateinit var viewPager: ViewPager

    val pageTitles = arrayOf("Welcome","Introduction", "Set Up Your Magical Zone", "Witness the True Magic","Shape the Magic Your Way")

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_information, container, false)
        viewPager = rootView.findViewById(R.id.viewPager)

        viewPager.adapter = PagerAdapter(pageTitles, childFragmentManager)
        val springDotsIndicator = rootView.findViewById<SpringDotsIndicator>(R.id.spring_dots_indicator)

        springDotsIndicator.setViewPager(viewPager)

        return rootView
    }

    override fun onNextClicked() {
        val nextPage = viewPager.currentItem + 1
        if (nextPage < pageTitles.size) {
            viewPager.setCurrentItem(nextPage, true)
        }
    }

    override fun onSkipClicked() {
        viewPager.setCurrentItem(pageTitles.size - 1, true)
    }
    inner class PagerAdapter(private val pageTitles: Array<String>, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

//        override fun getItem(position: Int): Fragment {
////            return PageFragment.newInstance(pageTitles[position])
//
//        }
override fun getItem(position: Int): Fragment {
    val fragment = PageFragment.newInstance(pageTitles[position])
    fragment.pageChangeListener = this@InformationFragment
    return fragment
}


        override fun getCount(): Int {
            return pageTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pageTitles[position]
        }

        override fun getItemPosition(obj: Any): Int {
            return POSITION_NONE
        }
    }
}