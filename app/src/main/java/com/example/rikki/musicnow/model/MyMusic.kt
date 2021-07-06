package com.example.rikki.musicnow.model

data class MyMusic(
        val id: String = "",
        val name: String = "",
        val desc: String = "",
        val photoUrl: String = "",
        val url: String = "",
        val format: String = "",
        var isFavorited: Boolean = false,
        var isDownloaded: Boolean = false
)
