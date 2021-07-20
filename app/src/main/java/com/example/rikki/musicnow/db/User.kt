package com.example.rikki.musicnow.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey @ColumnInfo(name = "mobile_number") val mobile: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email_address") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "fav_music") val favMusicList: String,
    @ColumnInfo(name = "fav_video") val favVideoList: String,
    @ColumnInfo(name = "fav_picture") val favPicList: String
)
