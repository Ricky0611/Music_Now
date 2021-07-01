package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentVideoDetailBinding
import com.example.rikki.musicnow.model.MyVideo

class VideoDetailFragment : Fragment() {

    private var binding: FragmentVideoDetailBinding? = null
    private val model: HomeViewModel by activityViewModels()
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
            model.containsVideo(it)
            model.getVideo().observe(viewLifecycleOwner, { response ->
                if (response.id.isBlank()) {
                    cannotDisplay()
                } else {
                    video = response
                    initUI()
                }
            })
        } ?: run {
            cannotDisplay()
        }

        return binding?.root
    }

    private fun initUI() {
        binding?.videoName?.text = video.name
        binding?.videoDesc?.text = video.desc
        binding?.startBtn?.isEnabled = false
        binding?.startBtn?.setOnClickListener {
            binding?.startBtn?.visibility = View.GONE
            binding?.videoView?.start()
        }
        // setup video
        controller = MediaController(requireActivity())
        binding?.videoView?.apply {
            setVideoPath(video.url)
            setMediaController(controller)
            setOnErrorListener { mp, what, extra ->
                Toast.makeText(requireActivity(), getString(R.string.video_play_error), Toast.LENGTH_SHORT).show()
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

    private fun cannotDisplay() {
        Toast.makeText(requireActivity(), getString(R.string.unavailable_video), Toast.LENGTH_LONG).show()
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
