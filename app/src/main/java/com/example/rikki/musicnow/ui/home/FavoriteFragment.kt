package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentFavoriteBinding
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.*

class FavoriteFragment : Fragment() {

    private var type: Int? = null
    private var binding: FragmentFavoriteBinding? = null
    private var musicList: ArrayList<MyMusic> = arrayListOf()
    private var videoList: ArrayList<MyVideo> = arrayListOf()
    private var picList: ArrayList<MyPicture> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(FAV_TYPE)
        }
        instance = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        // feature not available before user sign-in
        if (SPController.getInstance(requireActivity()).hasUserLoggedIn()) {
            binding?.listView?.isVisible = true
            binding?.unavailable?.isVisible = false
            when (type) {
                Constants.MUSIC_CODE -> showMusicList()
                Constants.VIDEO_CODE -> showVideoList()
                Constants.PICTURE_CODE -> showPictures()
            }
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

    private fun showPictures() {
        picList.clear()
        HomeActivity.picList.forEach {
            if (it.isFavorited)
                picList.add(it)
        }
        val pictureAdapter = PictureAdapter(picList, true) { id ->
            findNavController().navigate(
                R.id.action_picture_to_detail,
                bundleOf(PictureDetailFragment.ID to id)
            )
        }
        binding?.listView?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = pictureAdapter
        }
    }

    private fun showVideoList() {
        videoList.clear()
        HomeActivity.videoList.forEach {
            if (it.isFavorited)
                videoList.add(it)
        }
        val videoAdapter = VideoAdapter(videoList, true) {
            findNavController().navigate(
                R.id.action_video_to_detail,
                bundleOf(VideoDetailFragment.ID to it)
            )
        }
        binding?.listView?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = videoAdapter
        }
    }

    private fun showMusicList() {
        musicList.clear()
        HomeActivity.musicList.forEach {
            if (it.isFavorited)
                musicList.add(it)
        }
        val musicAdapter = MusicAdapter(musicList, true) {
            findNavController().navigate(
                R.id.action_music_to_detail,
                bundleOf(MusicDetailFragment.ID to it)
            )
        }
        binding?.listView?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = musicAdapter
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
        private lateinit var instance: FavoriteFragment
        private const val FAV_TYPE = "type"
        private lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

        @JvmStatic
        fun newInstance(type: Int) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(FAV_TYPE, type)
                }
            }

        fun refreshList(type: Int) {
            if (this::instance.isInitialized) {
                when (type) {
                    Constants.MUSIC_CODE -> instance.showMusicList()
                    Constants.VIDEO_CODE -> instance.showVideoList()
                    Constants.PICTURE_CODE -> instance.showPictures()
                }
            }
        }
    }

}
