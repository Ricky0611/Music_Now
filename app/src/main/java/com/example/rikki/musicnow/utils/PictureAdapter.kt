package com.example.rikki.musicnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.model.MyPicture
import com.squareup.picasso.Picasso

class PictureAdapter(private val list: ArrayList<MyPicture>, private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewHolder = view
        val imageView: ImageView = view.findViewById(R.id.picView)
        val titleTextView: TextView = view.findViewById(R.id.picTitle)
        val descTextView: TextView = view.findViewById(R.id.picDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_picture, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = list[position]
        Picasso.get().load(picture.url).placeholder(R.drawable.image_unavailable).error(R.drawable.image_unavailable).into(holder.imageView)
        holder.imageView.contentDescription = picture.desc
        holder.titleTextView.text = picture.title
        holder.descTextView.text = picture.desc
        holder.viewHolder.setOnClickListener {
            onItemClicked(picture.id)
        }
    }

    override fun getItemCount(): Int = list.size
}
