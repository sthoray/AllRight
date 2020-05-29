package com.sthoray.allright

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    private val delayTime: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({

            // Activity for Displaying license information
            //startActivity(Intent(this, OssLicensesMenuActivity::class.java))

            //val intent = Intent(this, BrowseActivity::class.java)
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
            finish()
        }, delayTime)
    }
}
