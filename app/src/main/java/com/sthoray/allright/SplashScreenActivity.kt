package com.sthoray.allright

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    private val delayTime: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({

            //val intent = Intent(this, BrowseActivity::class.java)
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
            finish()
        }, delayTime)
    }
}
