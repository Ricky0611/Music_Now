package com.example.rikki.musicnow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rikki.musicnow.databinding.ActivityHomeBinding
import com.example.rikki.musicnow.utils.SPController

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.nav_home
            ), drawer
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.apply {
            setupWithNavController(navController)
            menu.findItem(R.id.nav_member).isVisible = isUserActive
        }
        navHeader.apply {
            findViewById<LinearLayoutCompat>(R.id.userProfile).isVisible = isUserActive
            if (isUserActive) {
                SPController.getInstance(applicationContext).getUserName().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userName).text = it
                    }
                }
                SPController.getInstance(applicationContext).getUserEmail().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userEmail).text = it
                    }
                }
                SPController.getInstance(applicationContext).getUserMobile().let {
                    if (it.isNotBlank()) {
                        findViewById<TextView>(R.id.userMobile).text = it
                    }
                }
            }
        }

        // login alert dialog
        loginAlertDialog = AlertDialog.Builder(this).apply {
            setCancelable(false)
            setTitle(R.string.not_signin_yet)
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
}
