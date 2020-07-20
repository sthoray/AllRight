package com.sthoray.allright.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sthoray.allright.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The main activity where everything starts.
 *
 * Contains a bottom navigation bar to switch between commonly
 * used fragments.
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

        bottomNavigationView.setupWithNavController(
            navigationHostFragment.findNavController()
        )
    }
}