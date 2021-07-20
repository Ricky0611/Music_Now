package com.example.rikki.musicnow.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.rikki.musicnow.db.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SPController private constructor(context: Context) {

    init {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun hasUserLoggedIn(): Boolean {
        return settings.contains(PREFS_KEY_MOBILE)
    }

    fun saveUser(name: String, mobile: String, email: String, password: String) {
        settings.edit(commit = true) {
            this.putString(PREFS_KEY_NAME, name)
            this.putString(PREFS_KEY_MOBILE, mobile)
            this.putString(PREFS_KEY_EMAIL, email)
            this.putString(PREFS_KEY_PWD, password)
        }
        GlobalScope.launch {
            AppController.getUserDao().insert(User(mobile, name, email, password, "", "", ""))
        }
    }

    fun getUserName() : String {
        return settings.getString(PREFS_KEY_NAME, "") ?: ""
    }

    fun getUserMobile() : String {
        return settings.getString(PREFS_KEY_MOBILE, "") ?: ""
    }

    fun getUserEmail() : String {
        return settings.getString(PREFS_KEY_EMAIL, "") ?: ""
    }

    fun getUserPassword() : String {
        return settings.getString(PREFS_KEY_PWD, "") ?: ""
    }

    fun updatePassword(password: String) {
        settings.edit(commit = true) {
            this.putString(PREFS_KEY_PWD, password)
        }
    }

    fun deleteUser() {
        settings.edit(commit = true) {
            this.clear()
        }
    }

    companion object : SingletonHolder<SPController, Context>(::SPController) {
        private const val PREFS_NAME = "USER_INFO"
        private const val PREFS_KEY_NAME = "NAME"
        private const val PREFS_KEY_MOBILE = "MOBILE"
        private const val PREFS_KEY_EMAIL = "EMAIL"
        private const val PREFS_KEY_PWD = "PWD"

        private lateinit var settings: SharedPreferences
    }
}
