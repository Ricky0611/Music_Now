package com.example.rikki.musicnow.ui.home

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentMusicDetailBinding
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.utils.Constants
import com.example.rikki.musicnow.utils.Constants.music_fast_duration
import com.example.rikki.musicnow.utils.Constants.start_duration
import com.example.rikki.musicnow.utils.SPController
import com.squareup.picasso.Picasso
import java.util.*

class MusicDetailFragment : Fragment(), MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private var binding: FragmentMusicDetailBinding? = null
    private var id: String? = null
    private lateinit var musicRecord: MyMusic
    private lateinit var player: MediaPlayer
    private var isPaused = false
    private val model: HomeViewModel by activityViewModels()
    private val updater = Runnable {
        primarySeekBarProgressUpdater()
    }

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
            binding?.seekBar?.setOnSeekBarChangeListener(this)
            val name = HomeActivity.formatFileName(musicRecord.name, musicRecord.format)
            val path = (requireActivity() as HomeActivity).getAppSpecificMusicStorageDir(requireActivity(), name).absolutePath
            player.setDataSource(path)
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
                    Handler(Looper.getMainLooper()).removeCallbacks(updater)
                }
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                isPaused = true
            } else {
                binding?.playBtn?.setImageResource(R.drawable.ic_pause)
                if (isPaused) {
                    isPaused = false
                    player.start()
                    if (musicRecord.isDownloaded) {
                        primarySeekBarProgressUpdater()
                    }
                } else {
                    player.prepareAsync()
                }
            }
        }
        binding?.backBtn?.setOnClickListener {
            if (musicRecord.isDownloaded) {
                player.pause()
                Handler(Looper.getMainLooper()).removeCallbacks(updater)
                isPaused = true
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                if (player.currentPosition > music_fast_duration) {
                    player.seekTo(player.currentPosition - music_fast_duration)
                } else {
                    player.seekTo(0)
                }
                binding?.playBtn?.setImageResource(R.drawable.ic_pause)
                player.start()
                isPaused = false
                primarySeekBarProgressUpdater()
            } else {
                Toast.makeText(activity, R.string.music_cannot_control, Toast.LENGTH_SHORT).show()
            }
        }
        binding?.forwardBtn?.setOnClickListener {
            if (musicRecord.isDownloaded) {
                player.pause()
                Handler(Looper.getMainLooper()).removeCallbacks(updater)
                isPaused = true
                binding?.playBtn?.setImageResource(R.drawable.ic_play)
                if (player.currentPosition + music_fast_duration < player.duration) {
                    player.seekTo(player.currentPosition + music_fast_duration)
                } else {
                    player.seekTo(player.duration)
                }
                binding?.playBtn?.setImageResource(R.drawable.ic_pause)
                player.start()
                isPaused = false
                primarySeekBarProgressUpdater()
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
                    Handler(Looper.getMainLooper()).removeCallbacks(updater)
                    binding?.songCurrentDurationLabel?.text = millisecondToTime(0)
                    binding?.seekBar?.apply {
                        max = player.duration
                        progress = 0
                    }
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
            val name = HomeActivity.formatFileName(musicRecord.name, musicRecord.format)
            val path = requireActivity().filesDir.absolutePath
            Log.d("Music ${musicRecord.id}", "$path/$name")
            model.download(path, name, musicRecord.url)
            model.getDownloadProgress().observe(viewLifecycleOwner, { progress ->
                Log.d("Music ${musicRecord.id}", "progress = $progress")
                if (progress == Constants.MAX) {
                    musicRecord.isDownloaded = true
                    Toast.makeText(requireActivity(), R.string.download_status_success, Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_music_detail_reload, bundleOf(ID to id))
                }
            })
            model.getDownloadStatus().observe(viewLifecycleOwner, { msg ->
                Log.d("Music ${musicRecord.id}", "msg = $msg")
                val text = String.format(getString(R.string.download_status_failed), msg)
                Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun cannotPlay() {
        Toast.makeText(requireActivity(), R.string.unavailable_music, Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d("Music_Detail", "MediaPlayer.onPrepared")
        binding?.playBtn?.setImageResource(R.drawable.ic_pause)
        HomeActivity.endLoading()
        player.start()
        if (musicRecord.isDownloaded && mp != null) {
            binding?.seekBar?.apply {
                max = mp.duration
                progress = 0
            }
            binding?.songTotalDurationLabel?.text = millisecondToTime(mp.duration)
            binding?.songCurrentDurationLabel?.text = millisecondToTime(0)
            primarySeekBarProgressUpdater()
        }
    }

    private fun primarySeekBarProgressUpdater() {
        binding?.seekBar?.apply {
            max = player.duration
            progress = player.currentPosition
        }
        binding?.songCurrentDurationLabel?.text = millisecondToTime(player.currentPosition)
        if (player.isPlaying) {
            Handler(Looper.getMainLooper()).postDelayed(updater, start_duration)
        }
    }

    private fun millisecondToTime(time: Int): String {
        val hour = time / HOUR
        val minute = (time % HOUR) / MINUTE
        val second = (time % MINUTE) / SECOND
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        Log.d("Music_Detail", "MediaPlayer.onBufferingUpdate")
        if (musicRecord.isDownloaded) {
            binding?.seekBar?.secondaryProgress = percent
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d("Music_Detail", "MediaPlayer.onCompletion")
        if (musicRecord.isDownloaded && mp != null) {
            binding?.seekBar?.apply {
                max = mp.duration
                progress = 0
            }
            binding?.songCurrentDurationLabel?.text = millisecondToTime(0)
            Handler(Looper.getMainLooper()).removeCallbacks(updater)
        }
        player.start()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player.seekTo(progress)
            binding?.seekBar?.progress = progress
            binding?.songCurrentDurationLabel?.text = millisecondToTime(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        Handler(Looper.getMainLooper()).removeCallbacks(updater)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        primarySeekBarProgressUpdater()
    }

    override fun onDestroyView() {
        binding = null
        player.release()
        if (musicRecord.isDownloaded) {
            Handler(Looper.getMainLooper()).removeCallbacks(updater)
        }
        super.onDestroyView()
    }

    companion object {
        const val ID = "music_id"
        private const val HOUR = 60 * 60 * 1000
        private const val MINUTE = 60 * 1000
        private const val SECOND = 1000
    }
}
