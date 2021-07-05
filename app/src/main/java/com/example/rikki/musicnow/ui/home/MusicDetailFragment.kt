package com.example.rikki.musicnow.ui.home

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentMusicDetailBinding
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.utils.Constants.music_fast_duration
import com.example.rikki.musicnow.utils.SPController
import com.squareup.picasso.Picasso

class MusicDetailFragment : Fragment(), MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private var binding: FragmentMusicDetailBinding? = null
    private var id: String? = null
    private lateinit var musicRecord: MyMusic
    private lateinit var player: MediaPlayer
    private var isPaused = false

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
        binding = FragmentMusicDetailBinding.inflate(inflater, container, false)

        // get music and display
        id?.let { it ->
            HomeActivity.musicList.let { list ->
                for (i in list.indices) {
                    if (list[i].id == it) {
                        musicRecord = list[i]
                        break
                    }
                }
                display()
            }
        } ?: run {
            cannotPlay()
        }

        return binding?.root
    }

    private fun display() {
        if (::musicRecord.isInitialized) {
            binding?.musicDetailTitle?.text = musicRecord.name
            binding?.musicDetailDesc?.text = musicRecord.desc
            Picasso.get().load(musicRecord.photoUrl).placeholder(R.drawable.image_unavailable)
                .error(R.drawable.image_unavailable).into(
                    binding?.musicDetailImage
                )
            binding?.musicDetailImage?.contentDescription = musicRecord.name
            player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                )
                setOnPreparedListener(this@MusicDetailFragment)
                setOnBufferingUpdateListener(this@MusicDetailFragment)
                setOnCompletionListener(this@MusicDetailFragment)
            }
            initFunctionButtons()
            checkDownload()
        } else {
            cannotPlay()
        }
    }

    private fun checkDownload() {
        if (musicRecord.isDownloaded) {
            binding?.timezone?.visibility = View.VISIBLE
            // TODO: get file path and set source
        } else {
            binding?.timezone?.visibility = View.GONE
            player.setDataSource(musicRecord.url)
        }
        HomeActivity.startLoading()
        player.prepareAsync()
    }

    private fun initFunctionButtons() {
        binding?.playBtn?.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                if (musicRecord.isDownloaded) {
                    // TODO: update seekbar
                }
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                isPaused = true
            } else {
                binding?.playBtn?.setImageResource(R.drawable.ic_pause)
                if (isPaused) {
                    isPaused = false
                    player.start()
                    if (musicRecord.isDownloaded) {
                        // TODO: update seekbar
                    }
                } else {
                    player.prepareAsync()
                }
            }
        }
        binding?.backBtn?.setOnClickListener {
            if (musicRecord.isDownloaded) {
                player.pause()
                isPaused = true
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                if (player.currentPosition > music_fast_duration) {
                    player.seekTo(player.currentPosition - music_fast_duration)
                } else {
                    player.seekTo(0)
                }
                player.start()
                isPaused = false
                binding?.playBtn?.setImageResource(R.drawable.ic_pause)
            } else {
                Toast.makeText(activity, R.string.music_cannot_control, Toast.LENGTH_SHORT).show()
            }
        }
        binding?.forwardBtn?.setOnClickListener {
            if (musicRecord.isDownloaded) {
                //
            } else {
                Toast.makeText(activity, R.string.music_cannot_control, Toast.LENGTH_SHORT).show()
            }
        }
        binding?.stopBtn?.setOnClickListener {
            if (player.isPlaying || (!player.isPlaying && isPaused)) {
                player.stop()
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                isPaused = false
                if (musicRecord.isDownloaded) {
                    // TODO: set timezone and update seekbar
                }
            }
        }
        binding?.downloadBtn?.isVisible =
            SPController.getInstance(requireActivity()).hasUserLoggedIn()
        binding?.downloadBtn?.setOnClickListener {
            downloadMusic()
        }
    }

    private fun downloadMusic() {
        if (musicRecord.isDownloaded) {
            Toast.makeText(requireActivity(), R.string.music_downloaded, Toast.LENGTH_SHORT).show()
        } else {
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath?.let { root ->
                // TODO download and save under root
            } ?: run {
                Toast.makeText(requireActivity(), R.string.unavailable_path, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cannotPlay() {
        Toast.makeText(requireActivity(), R.string.unavailable_music, Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d("Music_Detail", "MediaPlayer.onPrepared")
        if (musicRecord.isDownloaded) {
            // TODO: set timezone and update seekbar
        }
        binding?.playBtn?.setImageResource(R.drawable.ic_pause)
        HomeActivity.endLoading()
        player.start()
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        Log.d("Music_Detail", "MediaPlayer.onBufferingUpdate")
        if (musicRecord.isDownloaded) {
            // TODO: update seekbar
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d("Music_Detail", "MediaPlayer.onCompletion")
        if (musicRecord.isDownloaded) {
            // TODO: set timezone and update seekbar
        }
        player.start()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val ID = "music_id"
    }
}
