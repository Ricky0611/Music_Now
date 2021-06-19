package com.example.rikki.musicnow.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rikki.musicnow.ui.home.FavoriteFragment
import com.example.rikki.musicnow.ui.home.ListFragment
import com.example.rikki.musicnow.ui.home.PictureFragment

class PagerAdapter(private val fm: Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(fm) {
            is PictureFragment -> {
                if (position == 1) FavoriteFragment()
                else ListFragment()
            }
            else -> FavoriteFragment()
        }
    }
}
