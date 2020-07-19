package com.sthoray.allright.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sthoray.allright.R

/**
 * The main activity where everything starts.
 *
 * This is the "home page" where the user us presented with suggestions to
 * start searching and can view their past search queries.
 */
class MainActivity : AppCompatActivity() {

    companion object {

        /** The key for a selected categoryId. */
        const val CATEGORY_ID_KEY = "CATEGORY_ID"
    }

    /**
     * Set up navigation bar.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navigationViewMain)
        val navController = findNavController(R.id.navHostFragmentMain)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_browse, R.id.navigation_profile
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}