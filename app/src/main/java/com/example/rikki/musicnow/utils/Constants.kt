package com.example.rikki.musicnow.utils

import java.util.regex.Pattern

object Constants {
    // request code
    const val INTERNET_REQUEST = 1
    // response status
    const val success = "success"
    const val fail = "failure"
    const val exist = "exsist"
    const val mismatch = "mismatch"
    const val picture_header = "Photo Story"
    // password pattern
    val passwordPattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,10}\$")
    // urls
    const val urlLogin = "http://rjtmobile.com/ansari//rjt_music/music_app/login.php?&user_mobile=%s&user_pass=%s"
    const val urlRegister = "http://rjtmobile.com/ansari/rjt_music/music_app/registration.php?&user_name=%s&user_email=%s&user_mobile=%s&user_pass=%s"
    const val urlReset = "http://rjtmobile.com/ansari/rjt_music/music_app/reset_pass.php?&user_mobile=%s&user_pass=%s&newpassword=%s"
    const val urlPictureList = "http://rjtmobile.com/ansari/rjt_music/music_app/pics_list.php?"
    const val urlPicture = "http://rjtmobile.com/ansari/rjt_music/music_app/pic_detail.php?&id=%s"
}
