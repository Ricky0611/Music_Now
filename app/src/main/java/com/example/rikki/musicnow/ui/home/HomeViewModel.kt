package com.example.rikki.musicnow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.music_header
import com.example.rikki.musicnow.utils.Constants.picture_header
import com.example.rikki.musicnow.utils.Constants.urlMusic
import com.example.rikki.musicnow.utils.Constants.urlMusicList_New
import com.example.rikki.musicnow.utils.Constants.urlMusicList_TopComp
import com.example.rikki.musicnow.utils.Constants.urlMusicList_TopPlayed
import com.example.rikki.musicnow.utils.Constants.urlPicture
import com.example.rikki.musicnow.utils.Constants.urlPictureList
import com.example.rikki.musicnow.utils.Constants.urlVideo
import com.example.rikki.musicnow.utils.Constants.urlVideoList
import com.example.rikki.musicnow.utils.Constants.video_header
import com.example.rikki.musicnow.utils.Constants.videos_header

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
                            picture.getString("PicUrl").replace(" ", "%20")
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
                        photo.getString("PicUrl").replace(" ", "%20")
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
                        music.getString("AlbumThumb").replace(" ", "%20"),
                        music.getString("MusicFile").replace(" ", "%20")
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
                        music.getString("AlbumThumb").replace(" ", "%20"),
                        music.getString("MusicFile").replace(" ", "%20")
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
                        music.getString("AlbumThumb").replace(" ", "%20"),
                        music.getString("MusicFile").replace(" ", "%20")
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
                    record.getString("AlbumThumb").replace(" ", "%20"),
                    record.getString("MusicFile").replace(" ", "%20")
                ))
            }
        }, { error ->
            Log.d("Music $id", "Error: ${error.localizedMessage}")
            music.postValue(MyMusic())
        })
        AppController.addToRequestQueue(req)
    }

    fun getMusic() : LiveData<MyMusic> = music

    private val videoList = MutableLiveData<ArrayList<MyVideo>>()
    private val video = MutableLiveData<MyVideo>()

    fun fetchVideoList() {
        val req = JsonObjectRequest(Request.Method.GET, urlVideoList, null, { response ->
            Log.d("Video_List", response.toString())
            val array = response.getJSONArray(videos_header)
            val list = arrayListOf<MyVideo>()
            for (i in 0 until array.length()) {
                array.getJSONObject(i).let { record ->
                    list.add(MyVideo(
                        record.getString("VideoId"),
                        record.getString("VideoName"),
                        record.getString("VideoDesc"),
                        record.getString("VideoThumb").replace(" ", "%20"),
                        record.getString("VideoFile").replace(" ", "%20")
                    ))
                }
            }
            videoList.postValue(list)
        }, { error ->
            Log.d("Video_List", "Error: ${error.localizedMessage}")
            videoList.postValue(arrayListOf())
        })
        AppController.addToRequestQueue(req)
    }

    fun getVideoList() : LiveData<ArrayList<MyVideo>> = videoList

    fun containsVideo(id: String) {
        videoList.value?.let { list ->
            for (i in list.indices) {
                if (list[i].id == id) {
                    video.postValue(list[i])
                }
            }
            fetchVideo(id)
        } ?: run {
            fetchVideo(id)
        }
    }

    private fun fetchVideo(id: String) {
        val url = String.format(urlVideo, id)
        val req = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.d("Video $id", response.toString())
            val array = response.getJSONArray(video_header)
            array.getJSONObject(0).let { record ->
                video.postValue(MyVideo(
                        record.getString("Id"),
                        record.getString("VideoName"),
                        record.getString("VideoDesc"),
                        record.getString("VideoThumb").replace(" ", "%20"),
                        record.getString("VideoFile").replace(" ", "%20")
                ))
            }
        }, { error ->
            Log.d("Video $id", "Error: ${error.localizedMessage}")
            video.postValue(MyVideo())
        })
        AppController.addToRequestQueue(req)
    }

    fun getVideo() : LiveData<MyVideo> = video
}
