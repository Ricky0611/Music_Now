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
import com.example.rikki.musicnow.utils.PictureAdapter

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val model: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        // show pictures
        showPictures()

        return binding?.root
    }

    private fun showPictures() {
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

}
