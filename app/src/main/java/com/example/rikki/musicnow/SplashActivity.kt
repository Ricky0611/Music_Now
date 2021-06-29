package com.example.rikki.musicnow

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.rikki.musicnow.databinding.ActivitySplashBinding
import com.example.rikki.musicnow.utils.Constants.fade_in_duration
import com.example.rikki.musicnow.utils.Constants.start_duration

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var fadeIn = AlphaAnimation(0.0f, 1.0f)

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
            duration = fade_in_duration
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.start.visibility = View.VISIBLE
                    }, start_duration)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }
        binding.fullscreenContent.apply {
            startAnimation(fadeIn)
        }

        // start
        binding.start.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
