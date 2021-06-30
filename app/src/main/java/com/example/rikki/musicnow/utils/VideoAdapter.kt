package com.example.rikki.musicnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.model.MyVideo
import com.squareup.picasso.Picasso

class VideoAdapter(private val list: ArrayList<MyVideo>, private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewHolder = itemView
        val imageView: ImageView = itemView.findViewById(R.id.video_poster)
        val titleView: TextView = itemView.findViewById(R.id.video_main_name)
        val descView: TextView = itemView.findViewById(R.id.video_main_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = list[position]
        holder.titleView.text = video.name
        holder.descView.text = video.desc
        Picasso.get().load(video.poster).placeholder(R.drawable.image_unavailable).error(R.drawable.image_unavailable).into(holder.imageView)
        holder.viewHolder.setOnClickListener {
            onItemClicked(video.id)
        }
    }

    override fun getItemCount(): Int = list.size
}
