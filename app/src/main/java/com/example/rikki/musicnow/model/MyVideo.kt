package com.example.rikki.musicnow.model

data class MyVideo(
    val id: String = "",
    val name: String = "",
    val desc: String = "",
    val poster: String = "",
    val url: String = "",
    val format: String = "",
    val isFavorited: Boolean = false,
    val isDownloaded: Boolean = false
)
