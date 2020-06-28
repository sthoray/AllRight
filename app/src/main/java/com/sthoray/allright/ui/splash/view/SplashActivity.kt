package com.sthoray.allright.ui.splash.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sthoray.allright.R
import com.sthoray.allright.ui.main.view.MainActivity

/**
 * Splash screen to briefly display brand identity.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}