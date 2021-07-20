package com.example.rikki.musicnow.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.model.MyMusic
import com.example.rikki.musicnow.model.MyPicture
import com.example.rikki.musicnow.model.MyVideo
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.MAX
import com.example.rikki.musicnow.utils.Constants.MAX_SIZE
import com.example.rikki.musicnow.utils.Constants.dot
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class HomeViewModel : ViewModel() {

    private val pictureList = MutableLiveData<ArrayList<MyPicture>>()
    private val picture = MutableLiveData<MyPicture>()

    fun fetchPictures() {
        if (HomeActivity.picList.isEmpty()) {
            val req = JsonObjectRequest(Request.Method.GET, urlPictureList, null, { response ->
                Log.d("Pictures", response.toString())
                val array = response.getJSONArray(picture_header)
                val list = arrayListOf<MyPicture>()
                for (i in 0 until array.length()) {
                    array.getJSONObject(i).let { picture ->
                        list.add(
                            MyPicture(
                                picture.getString("PicId"),
                                picture.getString("PicTitle"),
                                picture.getString("PicDesc"),
                                picture.getString("PicUrl").replace(" ", "%20"),
                                picture.getString("PicUrl").substringAfterLast(dot, "")
                            )
                        )
                    }
                }
                pictureList.postValue(list)
            }, { error ->
                Log.d("Pictures", "Error: ${error.localizedMessage}")
                pictureList.postValue(arrayListOf())
            })
            AppController.addToRequestQueue(req)
        }
    }

    fun getPictures() : LiveData<ArrayList<MyPicture>> = pictureList

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
                        photo.getString("PicUrl").replace(" ", "%20"),
                        photo.getString("PicUrl").substringAfterLast(dot, "")
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
        if (HomeActivity.musicList.isEmpty()) {
            fetchMusicNew()
            fetchMusicTopPlayed()
            fetchMusicTopComp()
        }
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
                        music.getString("MusicFile").replace(" ", "%20"),
                        music.getString("MusicFile").substringAfterLast(dot, "")
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
                        music.getString("MusicFile").replace(" ", "%20"),
                        music.getString("MusicFile").substringAfterLast(dot, "")
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
                        music.getString("MusicFile").replace(" ", "%20"),
                        music.getString("MusicFile").substringAfterLast(dot, "")
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
                    record.getString("MusicFile").replace(" ", "%20"),
                    record.getString("MusicFile").substringAfterLast(dot, "")
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
                        record.getString("VideoFile").replace(" ", "%20"),
                        record.getString("VideoFile").substringAfterLast(dot, "")
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
                        record.getString("VideoFile").replace(" ", "%20"),
                        record.getString("VideoFile").substringAfterLast(dot, "")
                ))
            }
        }, { error ->
            Log.d("Video $id", "Error: ${error.localizedMessage}")
            video.postValue(MyVideo())
        })
        AppController.addToRequestQueue(req)
    }

    fun getVideo() : LiveData<MyVideo> = video

    private val status = MutableLiveData<String>()
    private val progress = MutableLiveData<Int>()

    fun download(path: String, name: String, urlString: String) {
        HomeActivity.startLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL(urlString)
            var input: InputStream? = null
            var output: OutputStream? = null
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    status.postValue(connection.responseMessage)
                    progress.postValue(-1)
                    return@launch
                }
                input = connection.inputStream
                val file = File(path, name)
                output = FileOutputStream(file)
                val data = ByteArray(MAX_SIZE)
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    output.write(data, 0, count)
                }
                output.flush()
                progress.postValue(MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                progress.postValue(-1)
                status.postValue(e.localizedMessage)
            } finally {
                try {
                    input?.close()
                    output?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                connection?.disconnect()
                withContext(Dispatchers.Main) {
                    HomeActivity.endLoading()
                }
            }
        }
    }

    fun getDownloadStatus() : LiveData<String> = status
    fun getDownloadProgress() : LiveData<Int> = progress
}
