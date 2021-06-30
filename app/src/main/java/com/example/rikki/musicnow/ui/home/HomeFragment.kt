package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentHomeBinding
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.ImageAdapter
import com.example.rikki.musicnow.utils.SPController

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val model: HomeViewModel by activityViewModels()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // show specific features only when user has signed-in
        binding?.line2?.isVisible = SPController.getInstance(requireActivity()).hasUserLoggedIn()

        initNavigationButtons()

        displayPictures()

        return binding?.root
    }

    private fun initNavigationButtons() {
        // go to Music section
        binding?.musicBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_music)
        }

        // go to Video section
        binding?.videoBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_video)
        }

        // go to Picture section
        binding?.picBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_pic)
        }
    }

    private fun displayPictures() {
        if (HomeActivity.isInternetAvailable(requireActivity())) {
            HomeActivity.startLoading()
            model.fetchPictures()
            model.getPictures().observe(viewLifecycleOwner, { list ->
                if (list.isEmpty()) {
                    list.add(MyPicture("", "", getString(R.string.unavailable_image), ""))
                }
                imageAdapter = ImageAdapter(list)
                binding?.recyclerView?.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = imageAdapter
                }
                HomeActivity.endLoading()
            })
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
