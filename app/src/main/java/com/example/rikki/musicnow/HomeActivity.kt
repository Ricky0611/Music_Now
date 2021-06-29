package com.example.rikki.musicnow

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rikki.musicnow.databinding.ActivityHomeBinding
import com.example.rikki.musicnow.utils.Constants
import com.example.rikki.musicnow.utils.SPController

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this
        isInternetAvailable(this)

        initUI()
    }

    private fun initUI() {
        val toolbar = binding.homeBar.toolbar
        val fab = binding.homeBar.fab
        val drawer = binding.drawerLayout
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        val navHeader = binding.navView.getHeaderView(0)
        val isUserActive = SPController.getInstance(applicationContext).hasUserLoggedIn()

        // toolbar
        setSupportActionBar(toolbar)

        // floating button show when not signed-in
        fab.isVisible = !isUserActive
        fab.setOnClickListener { view ->
            loginAlertDialog.show()
        }

        // navigation drawer
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // navigation view
        val navController = fragment.navController
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_music, R.id.nav_picture, R.id.nav_reset
            ), drawer
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.apply {
            setupWithNavController(navController)
            menu.findItem(R.id.nav_member).isVisible = isUserActive
            menu.findItem(R.id.nav_logout).setOnMenuItemClickListener { menuItem ->
                showLogoutDialog()
                true
            }
        }
        navHeader.apply {
            findViewById<LinearLayoutCompat>(R.id.userProfile).isVisible = isUserActive
            if (isUserActive) {
                SPController.getInstance(applicationContext).getUserName().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userName).apply {
                            text = it
                            this.isVisible = true
                        }
                    }
                }
                SPController.getInstance(applicationContext).getUserEmail().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userEmail).apply {
                            text = it
                            this.isVisible = true
                        }
                    }
                }
                SPController.getInstance(applicationContext).getUserMobile().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userMobile).apply {
                            text = it
                            this.isVisible = true
                        }
                    }
                }
            }
        }

        // login alert dialog
        loginAlertDialog = AlertDialog.Builder(this).apply {
            setCancelable(false)
            setTitle(R.string.button_signIn)
            setMessage(R.string.signin_desc)
            setIcon(R.drawable.ic_info)
            setPositiveButton(R.string.button_signIn) { dialog, which ->
                dialog.dismiss()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton(R.string.button_cancel) { dialog, which ->
                dialog.cancel()
            }
        }.create()
    }

    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(this).apply {
            setCancelable(false)
            setTitle(R.string.drawer_logout)
            setMessage(R.string.logout_hint)
            setPositiveButton(R.string.button_logout) { dialog, which ->
                SPController.getInstance(this@HomeActivity).deleteUser()
                dialog.dismiss()
                recreate()
            }
            setNegativeButton(R.string.button_cancel) { dialog, which ->
                dialog.cancel()
            }
        }.create()
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_home_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun connectInternet() {
        val dialog = android.app.AlertDialog.Builder(this).apply {
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
            requestPermissions(arrayOf(Manifest.permission.INTERNET), Constants.INTERNET_REQUEST)
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
        if (requestCode == Constants.INTERNET_REQUEST && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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

    fun showProgressBar() {
        binding.homeBar.container.progressBar.let {
            if (!it.isVisible) {
                it.isVisible = true
            }
        }
    }

    fun hideProgressBar() {
        binding.homeBar.container.progressBar.let {
            if (it.isVisible) {
                it.isVisible = false
            }
        }
    }

    companion object {
        private lateinit var instance: HomeActivity

        fun isInternetAvailable(context: Context) : Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var isConnected = false
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val networkInfo = connectivityManager.activeNetworkInfo
                isConnected = networkInfo?.isConnectedOrConnecting == true
            } else {
                connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
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

        fun startLoading() {
            instance.showProgressBar()
        }

        fun endLoading() {
            instance.hideProgressBar()
        }

        fun login() {
            instance.loginAlertDialog.show()
        }
    }
}
