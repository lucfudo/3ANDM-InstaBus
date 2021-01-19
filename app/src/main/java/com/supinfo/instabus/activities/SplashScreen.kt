package com.supinfo.instabus.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.supinfo.instabus.R
import com.supinfo.instabus.SPLASH_TIME_OUT

class SplashScreen : AppCompatActivity() {
    /**
    Splash screen such as Instagram's.
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val intent = Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}