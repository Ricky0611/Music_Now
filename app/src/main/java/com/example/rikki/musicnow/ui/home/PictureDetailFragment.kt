package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentPictureDetailBinding
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.Constants
import com.example.rikki.musicnow.utils.SPController
import com.squareup.picasso.Picasso

class PictureDetailFragment : Fragment() {

    private var binding: FragmentPictureDetailBinding? = null
    private var id: String? = null
    private lateinit var picture: MyPicture
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
        binding = FragmentPictureDetailBinding.inflate(inflater, container, false)

        // get picture
        id?.let { it ->
            HomeActivity.picList.let { list ->
                for (i in list.indices) {
                    if (list[i].id == it) {
                        picture = list[i]
                        break
                    }
                }
                display()
            }
        } ?: run {
            cannotDisplay()
        }

        return binding?.root
    }

    private fun cannotDisplay() {
        Toast.makeText(requireActivity(), R.string.unavailable_picture, Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    private fun display() {
        if (::picture.isInitialized) {
            binding?.title?.text = picture.title
            binding?.description?.text = picture.desc
            Picasso.get().load(picture.url).placeholder(R.drawable.image_unavailable)
                .error(R.drawable.image_unavailable).into(
                    binding?.picture
                )
            binding?.downloadBtn?.isVisible =
                SPController.getInstance(requireActivity()).hasUserLoggedIn()
            binding?.downloadBtn?.setOnClickListener {
                downloadPicture()
            }
        } else {
            cannotDisplay()
        }
    }

    private fun downloadPicture() {
        if (picture.isDownloaded) {
            Toast.makeText(requireActivity(), R.string.picture_downloaded, Toast.LENGTH_LONG).show()
        } else {
            val name = HomeActivity.formatFileName(picture.title, picture.format)
            val path = requireActivity().filesDir.absolutePath
            Log.d("Picture ${picture.id}", "$path/$name")
            model.download(path, name, picture.url)
            model.getDownloadProgress().observe(viewLifecycleOwner, { progress ->
                Log.d("Picture ${picture.id}", "progress = $progress")
                if (progress == Constants.MAX) {
                    picture.isDownloaded = true
                    Toast.makeText(requireActivity(), R.string.download_status_success, Toast.LENGTH_LONG).show()
                }
            })
            model.getDownloadStatus().observe(viewLifecycleOwner, { msg ->
                Log.d("Picture ${picture.id}", "msg = $msg")
                val text = String.format(getString(R.string.download_status_failed), msg)
                Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val ID = "picture_id"
    }
}
