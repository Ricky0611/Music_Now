package com.example.rikki.musicnow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.model.MyPicture
import com.squareup.picasso.Picasso

class ImageAdapter(private val list: ArrayList<MyPicture>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val picture = list[position]
        Picasso.get().load(picture.url).placeholder(R.drawable.image_unavailable).error(R.drawable.image_unavailable).into(holder.imageView)
        holder.imageView.contentDescription = picture.desc
    }

    override fun getItemCount(): Int  = list.size
}
