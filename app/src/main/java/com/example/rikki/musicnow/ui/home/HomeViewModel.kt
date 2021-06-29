package com.example.rikki.musicnow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.music_header
import com.example.rikki.musicnow.utils.Constants.picture_header
import com.example.rikki.musicnow.utils.Constants.urlMusic
import com.example.rikki.musicnow.utils.Constants.urlMusicList_New
import com.example.rikki.musicnow.utils.Constants.urlMusicList_TopComp
import com.example.rikki.musicnow.utils.Constants.urlMusicList_TopPlayed
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

    private val musicListNew = MutableLiveData<ArrayList<MyMusic>>()
    private val musicListTopPlayed = MutableLiveData<ArrayList<MyMusic>>()
    private val musicListTopComp = MutableLiveData<ArrayList<MyMusic>>()
    private val music = MutableLiveData<MyMusic>()

    fun fetchMusicLists() {
        fetchMusicNew()
        fetchMusicTopPlayed()
        fetchMusicTopComp()
    }

    private fun fetchMusicTopComp() {
        val req = JsonObjectRequest(Request.Method.GET, urlMusicList_TopComp, null, { response ->
            Log.d("Music_TopComp", response.toString())
            val array = response.getJSONArray(music_header)
            val list = arrayListOf<MyMusic>()
            for (i in 0 until array.length()) {
                array.getJSONObject(i).let { music ->
                    list.add(MyMusic(
                        music.getString("AlbumId"),
                        music.getString("AlbumName"),
                        music.getString("AlbumDesc"),
                        music.getString("AlbumThumb"),
                        music.getString("MusicFile")
                    ))
                }
            }
            musicListTopComp.postValue(list)
        }, { error ->
            Log.d("Music_TopComp", "Error: ${error.localizedMessage}")
            musicListTopComp.postValue(arrayListOf())
        })
        AppController.addToRequestQueue(req)
    }

    private fun fetchMusicTopPlayed() {
        val req = JsonObjectRequest(Request.Method.GET, urlMusicList_TopPlayed, null, { response ->
            Log.d("Music_TopPlayed", response.toString())
            val array = response.getJSONArray(music_header)
            val list = arrayListOf<MyMusic>()
            for (i in 0 until array.length()) {
                array.getJSONObject(i).let { music ->
                    list.add(MyMusic(
                        music.getString("AlbumId"),
                        music.getString("AlbumName"),
                        music.getString("AlbumDesc"),
                        music.getString("AlbumThumb"),
                        music.getString("MusicFile")
                    ))
                }
            }
            musicListTopPlayed.postValue(list)
        }, { error ->
            Log.d("Music_TopPlayed", "Error: ${error.localizedMessage}")
            musicListTopPlayed.postValue(arrayListOf())
        })
        AppController.addToRequestQueue(req)
    }

    private fun fetchMusicNew() {
        val req = JsonObjectRequest(Request.Method.GET, urlMusicList_New, null, { response ->
            Log.d("Music_New", response.toString())
            val array = response.getJSONArray(music_header)
            val list = arrayListOf<MyMusic>()
            for (i in 0 until array.length()) {
                array.getJSONObject(i).let { music ->
                    list.add(MyMusic(
                        music.getString("AlbumId"),
                        music.getString("AlbumName"),
                        music.getString("AlbumDesc"),
                        music.getString("AlbumThumb"),
                        music.getString("MusicFile")
                    ))
                }
            }
            musicListNew.postValue(list)
        }, { error ->
            Log.d("Music_New", "Error: ${error.localizedMessage}")
            musicListNew.postValue(arrayListOf())
        })
        AppController.addToRequestQueue(req)
    }

    fun getMusicNew() : LiveData<ArrayList<MyMusic>> = musicListNew
    fun getMusicTopPlayed() : LiveData<ArrayList<MyMusic>> = musicListTopPlayed
    fun getMusicTopComp() : LiveData<ArrayList<MyMusic>> = musicListTopComp

    fun fetchMusic(id: String) {
        val url = String.format(urlMusic, id)
        val req = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.d("Music $id", response.toString())
            val array = response.getJSONArray(music_header)
            array.getJSONObject(0).let { record ->
                music.postValue(MyMusic(
                    record.getString("AlbumId"),
                    record.getString("AlbumName"),
                    record.getString("AlbumDesc"),
                    record.getString("AlbumThumb"),
                    record.getString("MusicFile")
                ))
            }
        }, { error ->
            Log.d("Music $id", "Error: ${error.localizedMessage}")
            music.postValue(MyMusic())
        })
        AppController.addToRequestQueue(req)
    }

    fun getMusic() : LiveData<MyMusic> = music
}
