package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentMyZoneBinding
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.MusicAdapter
import com.example.rikki.musicnow.utils.PictureAdapter
import com.example.rikki.musicnow.utils.VideoAdapter

class MyZoneFragment : Fragment() {

    private var binding: FragmentMyZoneBinding? = null
    private var musicList: ArrayList<MyMusic> = arrayListOf()
    private var videoList: ArrayList<MyVideo> = arrayListOf()
    private var picList: ArrayList<MyPicture> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyZoneBinding.inflate(inflater, container, false)

        instance = this

        getFavoriteList()

        return binding?.root
    }

    private fun getFavoriteList() {
        showMusic()
        showVideo()
        showPicture()
        displayEmptyMessage()
    }

    private fun displayEmptyMessage(){
        if (musicList.isEmpty() && videoList.isEmpty() && picList.isEmpty()) {
            Toast.makeText(requireActivity(), R.string.unavailable_favorite, Toast.LENGTH_LONG).show()
        }
    }

    private fun showPicture() {
        picList.clear()
        binding?.pictureList?.visibility = View.GONE
        HomeActivity.picList.forEach {
            if (it.isFavorited)
                picList.add(it)
        }
        if (picList.isNotEmpty()) {
            binding?.pictureList?.visibility = View.VISIBLE
            val pictureAdapter = PictureAdapter(picList, true) { id ->
                findNavController().navigate(
                    R.id.action_zone_to_picture_detail,
                    bundleOf(PictureDetailFragment.ID to id)
                )
            }
            binding?.pictureContent?.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = pictureAdapter
            }
        }
    }

    private fun showVideo() {
        videoList.clear()
        binding?.videoList?.visibility = View.GONE
        HomeActivity.videoList.forEach {
            if (it.isFavorited)
                videoList.add(it)
        }
        if (videoList.isNotEmpty()) {
            binding?.videoList?.visibility = View.VISIBLE
            val videoAdapter = VideoAdapter(videoList, true) {
                findNavController().navigate(
                    R.id.action_zone_to_video_detail,
                    bundleOf(VideoDetailFragment.ID to it)
                )
            }
            binding?.videoContent?.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = videoAdapter
            }
        }
    }

    private fun showMusic() {
        musicList.clear()
        binding?.musicList?.visibility = View.GONE
        HomeActivity.musicList.forEach {
            if (it.isFavorited)
                musicList.add(it)
        }
        if (musicList.isNotEmpty()) {
            binding?.musicList?.visibility = View.VISIBLE
            val musicAdapter = MusicAdapter(musicList, true) {
                findNavController().navigate(
                    R.id.action_zone_to_music_detail,
                    bundleOf(MusicDetailFragment.ID to it)
                )
            }
            binding?.musicContent?.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = musicAdapter
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        musicList.clear()
        videoList.clear()
        picList.clear()
        super.onDestroyView()
    }

    companion object {
        private lateinit var instance: MyZoneFragment

        fun refreshScreen() {
            if (this::instance.isInitialized) {
                instance.getFavoriteList()
            }
        }
    }
}
