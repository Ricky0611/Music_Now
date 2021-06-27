package com.example.rikki.musicnow.model

data class MyPicture(
        val id: String = "",
        val title: String = "",
        val desc: String = "",
        val url: String = "",
        val isFavorited: Boolean = false,
        val isDownloaded: Boolean = false
)
