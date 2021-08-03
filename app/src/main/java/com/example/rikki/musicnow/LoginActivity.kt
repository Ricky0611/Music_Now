package com.example.rikki.musicnow

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rikki.musicnow.ui.login.LoginFragment
import com.example.rikki.musicnow.utils.Constants.INTERNET_REQUEST
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        instance = this

        if (isInternetAvailable(this)) {
            FacebookSdk.fullyInitialize()
            AppEventsLogger.activateApp(application)
        }
    }

    private fun connectInternet() {
        val dialog = AlertDialog.Builder(this).apply {
            setIcon(R.drawable.ic_info)
            setTitle(R.string.attention)
            setMessage(R.string.no_internet)
            setCancelable(false)
            setPositiveButton(R.string.button_ok) { dialog, which ->
                dialog.dismiss()
            }
        }.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkInternet() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET), INTERNET_REQUEST)
        } else {
            connectInternet()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == INTERNET_REQUEST && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialog(permissions, requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionDialog(permissions: Array<out String>, requestCode: Int) {
        val msg = String.format(
            getString(R.string.permission_required_msg),
            permissions[0].substring(permissions[0].lastIndexOf(".") + 1)
        )
        val dialog = AlertDialog.Builder(this).apply {
            setIcon(R.drawable.ic_info)
            setTitle(R.string.permission_required_title)
            setMessage(msg)
            setCancelable(false)
            setPositiveButton(R.string.button_ok) { dialog, which ->
                dialog.dismiss()
                requestPermissions(permissions, requestCode)
            }
            setNegativeButton(R.string.button_exit) { dialog, which ->
                dialog.dismiss()
                finish()
            }
        }.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("LoginActivity", "onActivityResult - $requestCode, $resultCode, ${data?.extras.toString()}")
        supportFragmentManager.findFragmentById(R.id.nav_login_fragment)?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let {
                if (it is LoginFragment) {
                    it.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        private lateinit var instance: LoginActivity

        fun isInternetAvailable(context: Context) : Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var isConnected = false
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val networkInfo = connectivityManager.activeNetworkInfo
                isConnected = networkInfo?.isConnectedOrConnecting == true
            } else {
                connectivityManager.registerDefaultNetworkCallback(object :
                        ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        isConnected = true
                    }

                    override fun onLost(network: Network) {
                        isConnected = false
                    }
                })
            }
            if (!isConnected) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    instance.checkInternet()
                } else {
                    instance.connectInternet()
                }
            }
            return isConnected
        }

        fun showAlertDialog(msg: String) {
            val dialog = AlertDialog.Builder(instance).apply {
                setTitle(R.string.attention)
                setMessage(msg)
                setCancelable(false)
                setPositiveButton(R.string.button_ok) { dialog, which ->
                    dialog.dismiss()
                }
            }.create()
            dialog.show()
        }
    }
}
