package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.databinding.FragmentFavoriteBinding
import com.example.rikki.musicnow.utils.SPController

class FavoriteFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        // feature not available before user sign-in
        if (SPController.getInstance(requireActivity()).hasUserLoggedIn()) {
            binding?.listView?.isVisible = true
            binding?.unavailable?.isVisible = false
            Toast.makeText(requireActivity(), "Coming soon!", Toast.LENGTH_LONG).show()
        } else {
            binding?.unavailable?.isVisible = true
            binding?.listView?.isVisible = false
        }

        // sign-in
        binding?.loginBtn?.setOnClickListener {
            HomeActivity.login()
        }

        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
