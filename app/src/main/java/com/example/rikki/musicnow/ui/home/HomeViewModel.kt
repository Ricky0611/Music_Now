package com.example.rikki.musicnow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.urlPictureList

class HomeViewModel : ViewModel() {

    private val pictureList = MutableLiveData<ArrayList<MyPicture>>()

    fun fetchPictures() {
        val req = JsonObjectRequest(Request.Method.GET, urlPictureList, null, { response ->
            Log.d("Pictures", response.toString())
            val array = response.getJSONArray("Photo Story")
            val list = arrayListOf<MyPicture>()
            for (i in 0 until array.length()) {
                array.getJSONObject(i).let { picture ->
                    list.add(MyPicture(
                            picture.getString("PicId"),
                            picture.getString("PicTitle"),
                            picture.getString("PicDesc"),
                            picture.getString("PicUrl")
                    ))
                }
            }
            pictureList.postValue(list)
        }, { error ->
            Log.d("Pictures", "Error: ${error.localizedMessage}")
            pictureList.postValue(arrayListOf())
        })
        AppController.addToRequestQueue(req)
    }

    fun getPictures() : LiveData<ArrayList<MyPicture>> = pictureList

}
