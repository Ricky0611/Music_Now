package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.utils.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PictureFragment : Fragment() {

    private lateinit var adapter: PagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init swipe views
        adapter = PagerAdapter(this)
        viewPager = view.findViewById(R.id.picPager)
        viewPager.adapter = adapter
        // add tabs
        val tabLayout = view.findViewById<TabLayout>(R.id.picTabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                1 -> "Favorites" // todo: replace with string resources
                else -> "Picture"
            }
        }.attach()
    }

}
