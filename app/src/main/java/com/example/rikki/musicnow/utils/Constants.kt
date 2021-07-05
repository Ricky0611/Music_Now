package com.example.rikki.musicnow.utils

import java.util.regex.Pattern

object Constants {
    // milliseconds
    const val fade_in_duration = 4000L
    const val start_duration = 2000L
    const val music_fast_duration = 5000
    // request code
    const val INTERNET_REQUEST = 1
    // function code
    const val MUSIC_CODE = 1
    const val VIDEO_CODE = 2
    const val PICTURE_CODE = 3
    // response status
    const val success = "success"
    const val fail = "failure"
    const val exist = "exsist"
    const val mismatch = "mismatch"
    const val picture_header = "Photo Story"
    const val music_header = "Albums"
    const val videos_header = "Videos"
    const val video_header = "Video"
    const val dot = "."
    // password pattern
    val passwordPattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,10}\$")
    // urls
    const val urlLogin = "http://rjtmobile.com/ansari//rjt_music/music_app/login.php?&user_mobile=%s&user_pass=%s"
    const val urlRegister = "http://rjtmobile.com/ansari/rjt_music/music_app/registration.php?&user_name=%s&user_email=%s&user_mobile=%s&user_pass=%s"
    const val urlReset = "http://rjtmobile.com/ansari/rjt_music/music_app/reset_pass.php?&user_mobile=%s&user_pass=%s&newpassword=%s"
    const val urlPictureList = "http://rjtmobile.com/ansari/rjt_music/music_app/pics_list.php?"
    const val urlPicture = "http://rjtmobile.com/ansari/rjt_music/music_app/pic_detail.php?&id=%s"
    const val urlMusicList_New = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=new"
    const val urlMusicList_TopPlayed = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=top-played"
    const val urlMusicList_TopComp = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=top-comp"
    const val urlMusic = "http://rjtmobile.com/ansari/rjt_music/music_app/music_play.php?&id=%s"
    const val urlVideoList = "http://rjtmobile.com/ansari/rjt_music/music_app/video_list.php?"
    const val urlVideo = "http://rjtmobile.com/ansari/rjt_music/music_app/video_file.php?&id=%s"
}
