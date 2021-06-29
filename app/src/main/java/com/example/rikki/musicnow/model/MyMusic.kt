package com.example.rikki.musicnow.model

data class MyMusic(
        val id: String = "",
        val name: String = "",
        val desc: String = "",
        val photoUrl: String = "",
        val url: String = "",
        val isFavorited: Boolean = false,
        val isDownloaded: Boolean = false
)
