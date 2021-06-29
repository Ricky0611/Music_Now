package com.example.rikki.musicnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.model.MyMusic
import com.squareup.picasso.Picasso

class MusicAdapter(private val list: ArrayList<MyMusic>, private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewHolder = itemView
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val desc: TextView = itemView.findViewById(R.id.musicDesc)
        val photo: ImageView = itemView.findViewById(R.id.musicPic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = list[position]
        holder.title.text = music.name
        holder.desc.text = music.desc
        Picasso.get().load(music.photoUrl).placeholder(R.drawable.image_unavailable).error(R.drawable.image_unavailable).into(holder.photo)
        holder.viewHolder.setOnClickListener {
            onItemClicked(music.id)
        }
    }

    override fun getItemCount(): Int = list.size
}