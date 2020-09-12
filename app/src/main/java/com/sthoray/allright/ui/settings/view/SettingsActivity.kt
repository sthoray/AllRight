package com.sthoray.allright.ui.settings.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.sthoray.allright.R

/**
 * Activity to adjust the app's preferences and view additional information.
 */
class SettingsActivity : AppCompatActivity() {

    /**
     * Add the preferences fragment to this activity.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * React to the actionbar's back button.
     *
     * @param item The [MenuItem] was selected in the action bar.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Respond to the action bar's Up/Home button
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Fragment to contain the preference views.
     */
    class SettingsFragment : PreferenceFragmentCompat() {

        /**
         * Inflate the hierarchy from an XML resource.
         *
         * @param savedInstanceState If the fragment is being re-created from a previous
         * savedk state, this is the state.
         * @param rootKey If non-null, this preference fragment should be rooted at the
         * PreferenceScreen with this key.
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}