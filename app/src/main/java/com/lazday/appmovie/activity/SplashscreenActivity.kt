package com.lazday.appmovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.lazday.appmovie.R
import com.lazday.appmovie.fragment.HomeActivity

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar!!.hide()

        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        },2000)
    }
}