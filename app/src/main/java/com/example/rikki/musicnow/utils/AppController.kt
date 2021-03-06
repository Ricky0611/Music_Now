package com.example.rikki.musicnow.utils

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.rikki.musicnow.db.AppDatabase
import com.example.rikki.musicnow.db.UserDao

class AppController : Application() {

    init {
        instance = this
    }

    companion object {
        private const val TAG = "AppController"
        private lateinit var instance : AppController
        private lateinit var requestQueue: RequestQueue
        private lateinit var db: AppDatabase

        private fun getRequestQueue() : RequestQueue {
            if (!::requestQueue.isInitialized) {
                requestQueue = Volley.newRequestQueue(instance)
            }
            return requestQueue
        }

        fun <T> addToRequestQueue(req: Request<T>, tag: String? = null) {
            req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
            getRequestQueue().add(req)
        }

        fun initializeDB(context: Context) {
            db = AppDatabase.invoke(context)
        }

        fun getUserDao(): UserDao {
            return db.userDao()
        }
    }
}
