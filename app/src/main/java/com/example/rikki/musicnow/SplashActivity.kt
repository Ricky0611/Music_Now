package com.example.rikki.musicnow

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.rikki.musicnow.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var fadeIn = AlphaAnimation(0.0f, 1.0f)
    private var mVisible = false // visibility of start button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // animation
        fadeIn.apply {
            duration = 4000
            fillAfter = true
        }
        binding.fullscreenContent.apply {
            startAnimation(fadeIn)
            setOnClickListener {
                toggle()
            }
        }
        // start
        binding.start.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun toggle() {
        if (mVisible) { // hide button
            binding.start.visibility = View.GONE
            mVisible = false
        } else { // show button
            binding.start.visibility = View.VISIBLE
            mVisible = true
        }
    }
}
