package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentVideoDetailBinding
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.Constants.MAX
import com.example.rikki.musicnow.utils.SPController

class VideoDetailFragment : Fragment() {

    private var binding: FragmentVideoDetailBinding? = null
    private var id: String? = null
    private lateinit var video : MyVideo
    private lateinit var controller: MediaController
    private val model: HomeViewModel by activityViewModels()

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
            val url = if (video.isDownloaded) {
                val name = HomeActivity.formatFileName(video.name, video.format)
                (requireActivity() as HomeActivity).getAppSpecificVideoStorageDir(
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
            val name = HomeActivity.formatFileName(video.name, video.format)
            val path = requireActivity().filesDir.absolutePath
            Log.d("Video ${video.id}", "$path/$name")
            model.download(path, name, video.url)
            model.getDownloadProgress().observe(viewLifecycleOwner, { progress ->
                Log.d("Video ${video.id}", "progress = $progress")
                if (progress == MAX) {
                    video.isDownloaded = true
                    Toast.makeText(requireActivity(), R.string.download_status_success, Toast.LENGTH_LONG).show()
                }
            })
            model.getDownloadStatus().observe(viewLifecycleOwner, { msg ->
                Log.d("Video ${video.id}", "msg = $msg")
                val text = String.format(getString(R.string.download_status_failed), msg)
                Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
            })
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
