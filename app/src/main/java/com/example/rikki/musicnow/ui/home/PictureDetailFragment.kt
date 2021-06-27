package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentPictureDetailBinding
import com.example.rikki.musicnow.model.MyPicture
import com.squareup.picasso.Picasso

class PictureDetailFragment : Fragment() {

    private var binding: FragmentPictureDetailBinding? = null
    private val model: HomeViewModel by activityViewModels()
    private var id: String? = null

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
            model.containsPicture(it)
            model.getPicture().observe(viewLifecycleOwner, { response ->
                if (response.id.isBlank()) {
                    cannotDisplay()
                } else {
                    display(response)
                }
            })
        } ?: run {
            cannotDisplay()
        }

        return binding?.root
    }

    private fun cannotDisplay() {
        Toast.makeText(requireActivity(), getString(R.string.unavailable_picture), Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    private fun display(picture: MyPicture) {
        binding?.title?.text = picture.title
        binding?.description?.text = picture.desc
        Picasso.get().load(picture.url).placeholder(R.drawable.image_unavailable).error(R.drawable.image_unavailable).into(binding?.picture)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val ID = "picture_id"
    }
}
