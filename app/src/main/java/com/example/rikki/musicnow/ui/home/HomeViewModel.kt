package com.example.rikki.musicnow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.picture_header
import com.example.rikki.musicnow.utils.Constants.urlPicture
import com.example.rikki.musicnow.utils.Constants.urlPictureList

class HomeViewModel : ViewModel() {

    private val pictureList = MutableLiveData<ArrayList<MyPicture>>()
    private val picture = MutableLiveData<MyPicture>()

    fun fetchPictures() {
        val req = JsonObjectRequest(Request.Method.GET, urlPictureList, null, { response ->
            Log.d("Pictures", response.toString())
            val array = response.getJSONArray(picture_header)
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

    fun containsPicture(id: String) {
        pictureList.value?.let { list ->
            for (i in list.indices) {
                if (list[i].id == id) {
                    picture.postValue(list[i])
                }
            }
            fetchPicture(id)
        } ?: run {
            fetchPicture(id)
        }
    }

    private fun fetchPicture(id: String) {
        val url = String.format(urlPicture, id)
        val req = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.d("Picture $id", response.toString())
            val array = response.getJSONArray(picture_header)
            array.getJSONObject(0).let { photo ->
                picture.postValue(MyPicture(
                        photo.getString("PicId"),
                        photo.getString("PicTitle"),
                        photo.getString("PicDesc"),
                        photo.getString("PicUrl")
                ))
            }
        }, { error ->
            Log.d("Picture $id", "Error: ${error.localizedMessage}")
            picture.postValue(MyPicture())
        })
        AppController.addToRequestQueue(req)
    }

    fun getPicture() : LiveData<MyPicture> = picture
}
