package com.example.bthome.fragments


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.bthome.R



class InformationFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_information, container, false)
        viewPager = rootView.findViewById(R.id.viewPager)

        val pageTitles = arrayOf("Welcome","Introduction", "Set Up Your Magical Zone", "Witness the True Magic","Shape the Magic Your Way")
        val pagerAdapter = PagerAdapter(pageTitles, childFragmentManager)
        viewPager.adapter = pagerAdapter

        return rootView
    }

    inner class PagerAdapter(private val pageTitles: Array<String>, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return PageFragment.newInstance(pageTitles[position])
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