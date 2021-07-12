package com.example.rikki.musicnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.ui.home.FavoriteFragment
import com.example.rikki.musicnow.ui.home.ListFragment
import com.squareup.picasso.Picasso

class VideoAdapter(private val list: ArrayList<MyVideo>, private val isLogin: Boolean, private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewHolder = itemView
        val imageView: ImageView = itemView.findViewById(R.id.video_poster)
        val titleView: TextView = itemView.findViewById(R.id.video_main_name)
        val descView: TextView = itemView.findViewById(R.id.video_main_desc)
        val favBtn: ImageView = itemView.findViewById(R.id.favBtn)
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
        if (isLogin) {
            holder.favBtn.visibility = View.VISIBLE
            if (video.isFavorited) {
                holder.favBtn.setImageResource(R.drawable.favorite_on)
            } else {
                holder.favBtn.setImageResource(R.drawable.favorite_off)
            }
            holder.favBtn.setOnClickListener {
                if (video.isFavorited) {
                    holder.favBtn.setImageResource(R.drawable.favorite_off)
                    video.isFavorited = false
                } else {
                    holder.favBtn.setImageResource(R.drawable.favorite_on)
                    video.isFavorited = true
                }
                FavoriteFragment.refreshList(Constants.VIDEO_CODE)
                ListFragment.refreshList(Constants.VIDEO_CODE)
            }
        } else {
            holder.favBtn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = list.size
}
