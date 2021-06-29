package com.example.rikki.musicnow.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rikki.musicnow.ui.home.FavoriteFragment
import com.example.rikki.musicnow.ui.home.ListFragment
import com.example.rikki.musicnow.ui.home.MusicFragment
import com.example.rikki.musicnow.ui.home.PictureFragment
import com.example.rikki.musicnow.utils.Constants.MUSIC_CODE
import com.example.rikki.musicnow.utils.Constants.PICTURE_CODE

class PagerAdapter(private val fm: Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(fm) {
            is PictureFragment -> {
                if (position == 1) FavoriteFragment.newInstance(PICTURE_CODE)
                else ListFragment.newInstance(PICTURE_CODE)
            }
            is MusicFragment -> {
                if (position == 1) FavoriteFragment.newInstance(MUSIC_CODE)
                else ListFragment.newInstance(MUSIC_CODE)
            }
            else -> FavoriteFragment.newInstance(0)
        }
    }
}
