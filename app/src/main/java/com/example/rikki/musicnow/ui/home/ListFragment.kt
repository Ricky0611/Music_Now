package com.example.rikki.musicnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentListBinding
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.Constants.MUSIC_CODE
import com.example.rikki.musicnow.utils.Constants.PICTURE_CODE
import com.example.rikki.musicnow.utils.MusicAdapter
import com.example.rikki.musicnow.utils.PictureAdapter

class ListFragment : Fragment() {

    private var type: Int? = null
    private var binding: FragmentListBinding? = null
    private val model: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(LIST_TYPE)
            when (type) {
                MUSIC_CODE -> model.fetchMusicLists()
                PICTURE_CODE -> model.fetchPictures()
            }
        } ?: run {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        when (type) {
            MUSIC_CODE -> showMusicLists()
            PICTURE_CODE -> showPictures()
        }

        return binding?.root
    }

    private fun showMusicLists() {
        // change view
        binding?.musicListView?.visibility = View.VISIBLE
        binding?.mainRecyclerView?.visibility = View.GONE
        // show data
        model.getMusicNew().observe(viewLifecycleOwner, { list ->
            val musicAdapter = MusicAdapter(list) {
                findNavController().navigate(
                    R.id.action_music_to_detail,
                    bundleOf(MusicDetailFragment.ID to it)
                )
            }
            binding?.musicContentNew?.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = musicAdapter
            }
        })
        model.getMusicTopPlayed().observe(viewLifecycleOwner, { list ->
            val musicAdapter = MusicAdapter(list) {
                findNavController().navigate(
                    R.id.action_music_to_detail,
                    bundleOf(MusicDetailFragment.ID to it)
                )
            }
            binding?.musicContentTopPlayed?.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = musicAdapter
            }
        })
        model.getMusicTopComp().observe(viewLifecycleOwner, { list ->
            val musicAdapter = MusicAdapter(list) {
                findNavController().navigate(
                    R.id.action_music_to_detail,
                    bundleOf(MusicDetailFragment.ID to it)
                )
            }
            binding?.musicContentTopComp?.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = musicAdapter
            }
        })
    }

    private fun showPictures() {
        // change view
        binding?.musicListView?.visibility = View.GONE
        binding?.mainRecyclerView?.visibility = View.VISIBLE
        // show data
        model.getPictures().observe(viewLifecycleOwner, { list ->
            if (list.isEmpty()) {
                list.add(MyPicture("", "", getString(R.string.unavailable_image), ""))
            }
            val pictureAdapter = PictureAdapter(list) { id ->
                findNavController().navigate(
                    R.id.action_picture_to_detail,
                    bundleOf(PictureDetailFragment.ID to id)
                )
            }
            binding?.mainRecyclerView?.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = pictureAdapter
            }
        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {

        private const val LIST_TYPE = "type"

        @JvmStatic
        fun newInstance(type: Int) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(LIST_TYPE, type)
                }
            }
    }

}
