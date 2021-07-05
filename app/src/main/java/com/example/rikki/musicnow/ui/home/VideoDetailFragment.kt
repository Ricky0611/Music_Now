package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentVideoDetailBinding
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.Constants.dot
import com.example.rikki.musicnow.utils.SPController

class VideoDetailFragment : Fragment() {

    private var binding: FragmentVideoDetailBinding? = null
    private var id: String? = null
    private lateinit var video : MyVideo
    private lateinit var controller: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoDetailBinding.inflate(inflater, container, false)

        // get video
        id?.let {
            HomeActivity.videoList.let { list ->
                for (i in list.indices) {
                    if (list[i].id == it) {
                        video = list[i]
                        break
                    }
                }
                initUI()
            }
        } ?: run {
            cannotDisplay()
        }

        return binding?.root
    }

    private fun initUI() {
        if (::video.isInitialized) {
            binding?.videoName?.text = video.name
            binding?.videoDesc?.text = video.desc
            binding?.startBtn?.isEnabled = false
            binding?.startBtn?.setOnClickListener {
                binding?.startBtn?.visibility = View.GONE
                binding?.videoView?.start()
            }
            // setup download button
            binding?.downloadBtn?.isVisible =
                SPController.getInstance(requireActivity()).hasUserLoggedIn()
            binding?.downloadBtn?.setOnClickListener {
                downloadVideo()
            }
            // setup video
            setupVideoPlayer()
        } else {
            cannotDisplay()
        }
    }

    private fun setupVideoPlayer() {
        controller = MediaController(requireActivity())
        binding?.videoView?.apply {
            val url = if (video.isDownloaded && HomeActivity.isDownloadAvailable()) {
                val name = HomeActivity.formatFileName(video.name).plus(dot).plus(video.format)
                (requireActivity() as HomeActivity).getAppSpecificMovieStorageDir(
                    requireActivity(),
                    name
                ).absolutePath
            } else {
                video.url
            }
            setVideoPath(url)
            setMediaController(controller)
            setOnErrorListener { mp, what, extra ->
                Toast.makeText(requireActivity(), R.string.video_play_error, Toast.LENGTH_SHORT).show()
                false
            }
            setOnPreparedListener { player ->
                player.setOnVideoSizeChangedListener { mp, width, height ->
                    controller.setAnchorView(binding?.videoView)
                }
                binding?.startBtn?.isEnabled = true
            }
            setOnCompletionListener {
                binding?.startBtn?.visibility = View.VISIBLE
            }
        }
    }

    private fun downloadVideo() {
        if (video.isDownloaded) {
            Toast.makeText(requireActivity(), R.string.video_downloaded, Toast.LENGTH_LONG).show()
        } else {
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath?.let { root ->
                // TODO download and save under root
            } ?: run {
                Toast.makeText(requireActivity(), R.string.unavailable_path, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cannotDisplay() {
        Toast.makeText(requireActivity(), R.string.unavailable_video, Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val ID = "video_id"
    }
}
