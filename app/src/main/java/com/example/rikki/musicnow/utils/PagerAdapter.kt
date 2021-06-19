package com.example.rikki.musicnow.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rikki.musicnow.ui.home.PictureFragment
import com.example.rikki.musicnow.ui.login.LoginFragment
import com.example.rikki.musicnow.ui.login.RegisterFragment

class PagerAdapter(private val fm: Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(fm) {
            is PictureFragment -> {
                // todo: picture main (0) / picture fav (1)
                RegisterFragment()
            }
            else -> LoginFragment()
        }
    }
}
