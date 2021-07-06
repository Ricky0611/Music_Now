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
import com.example.rikki.musicnow.databinding.FragmentOfflineBinding
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.MusicAdapter
import com.example.rikki.musicnow.utils.PictureAdapter
import com.example.rikki.musicnow.utils.VideoAdapter

class OfflineFragment : Fragment() {

    private var binding: FragmentOfflineBinding? = null
    private var musicList: ArrayList<MyMusic> = arrayListOf()
    private var videoList: ArrayList<MyVideo> = arrayListOf()
    private var picList: ArrayList<MyPicture> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfflineBinding.inflate(inflater, container, false)

        getDownloadedResources()

        return binding?.root
    }

    private fun getDownloadedResources() {
        HomeActivity.musicList.forEach {
            if (it.isDownloaded)
                musicList.add(it)
        }
        showMusic()
        HomeActivity.videoList.forEach {
            if (it.isDownloaded)
                videoList.add(it)
        }
        showVideo()
        HomeActivity.picList.forEach {
            if (it.isDownloaded)
                picList.add(it)
        }
        showPicture()
        if (musicList.isEmpty() && videoList.isEmpty() && picList.isEmpty()) {
            Toast.makeText(requireActivity(), R.string.unavailable_offline, Toast.LENGTH_LONG).show()
        }
    }

    private fun showPicture() {
        if (picList.isEmpty()) {
            binding?.pictureList?.visibility = View.GONE
        } else {
            val pictureAdapter = PictureAdapter(picList) { id ->
                findNavController().navigate(
                    R.id.action_offline_to_picture_detail,
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
        if (videoList.isEmpty()) {
            binding?.videoList?.visibility = View.GONE
        } else {
            val videoAdapter = VideoAdapter(videoList) {
                findNavController().navigate(
                    R.id.action_offline_to_video_detail,
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
        if (musicList.isEmpty()) {
            binding?.musicList?.visibility = View.GONE
        } else {
            val musicAdapter = MusicAdapter(musicList) {
                findNavController().navigate(
                    R.id.action_offline_to_music_detail,
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
        super.onDestroyView()
    }

}
