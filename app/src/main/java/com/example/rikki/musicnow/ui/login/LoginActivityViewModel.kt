package com.example.rikki.musicnow.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.rikki.musicnow.utils.AppController
import com.example.rikki.musicnow.utils.Constants.urlLogin
import com.example.rikki.musicnow.utils.Constants.urlRegister
import com.example.rikki.musicnow.utils.Constants.urlReset
import kotlinx.coroutines.launch
import org.json.JSONArray

class LoginActivityViewModel : ViewModel() {

    private val loginResponse = MutableLiveData<JSONArray>()
    private val registerResponse = MutableLiveData<String>()
    private val resetResponse = MutableLiveData<String>()

    fun login(mobile: String, password: String) {
        val url = String.format(urlLogin, mobile, password)
        viewModelScope.launch {
            val req = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                Log.d("Login", response.toString())
                val msg = response.getJSONArray("msg")
                loginResponse.postValue(msg)
            }, { error ->
                Log.d("Login", "Error: ${error.localizedMessage}")
                loginResponse.postValue(JSONArray(error.localizedMessage) )
            })
            AppController.addToRequestQueue(req)
        }
    }

    fun getLoginResponse() : LiveData<JSONArray> {
        return loginResponse
    }

    fun register(name: String, email: String, mobile: String, password: String) {
        val url = String.format(urlRegister, name, email, mobile, password)
        viewModelScope.launch {
            val request = StringRequest(Request.Method.GET, url, { response ->
                Log.d("Register", response)
                registerResponse.postValue(response)
            }, { error ->
                Log.d("Register", "Error: ${error.localizedMessage}")
                registerResponse.postValue(error.localizedMessage)
            })
            AppController.addToRequestQueue(request)
        }
    }

    fun getRegisterResponse() : LiveData<String> {
        return registerResponse
    }

    fun resetPassword(mobile: String, oldPswd: String, newPswd: String) {
        val url = String.format(urlReset, mobile, oldPswd, newPswd)
        viewModelScope.launch {
            val req = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                Log.d("Reset", response.toString())
                val msg = response.getJSONArray("msg").getString(0)
                resetResponse.postValue(msg)
            }, { error ->
                Log.d("Reset", "Error: ${error.localizedMessage}")
                resetResponse.postValue(error.localizedMessage)
            })
            AppController.addToRequestQueue(req)
        }
    }

    fun getResetResponse() : LiveData<String> {
        return resetResponse
    }
}
