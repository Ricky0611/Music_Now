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

 class MusicFragment : Fragment() {

    private lateinit var adapter: PagerAdapter
     private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         // init swipe views
         adapter = PagerAdapter(this)
         viewPager = view.findViewById(R.id.musicPager)
         viewPager.adapter = adapter
         // add tabs
         val tabLayout = view.findViewById<TabLayout>(R.id.musicTabLayout)
         TabLayoutMediator(tabLayout, viewPager) { tab, position ->
             tab.text = when(position) {
                 1 -> getString(R.string.favorites)
                 else -> getString(R.string.drawer_music)
             }
         }.attach()
     }
}
