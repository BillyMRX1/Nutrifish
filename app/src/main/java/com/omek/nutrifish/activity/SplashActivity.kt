package com.omek.nutrifish.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.omek.nutrifish.MainActivity
import com.omek.nutrifish.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val appLogo = findViewById<ImageView>(R.id.logo)
        val tvTitle = findViewById<TextView>(R.id.title)
        appLogo.startAnimation(fadeIn)
        tvTitle.startAnimation(fadeIn)
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            val doneLogin = sharedPreferences.getString("Login", "")
            if (doneLogin.equals("Yes")){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }
}