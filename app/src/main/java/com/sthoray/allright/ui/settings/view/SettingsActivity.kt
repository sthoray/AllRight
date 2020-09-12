package com.sthoray.allright.ui.settings.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
         * saved state, this is the state.
         * @param rootKey If non-null, this preference fragment should be rooted at the
         * PreferenceScreen with this key.
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val logoutPreference: Preference? = findPreference("logout")
            logoutPreference?.setOnPreferenceClickListener {
                logout()
                true
            }
        }

        private fun logout() {
            context?.let { context ->
                val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    getString(R.string.preference_crypt_auth_key),
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

                encryptedSharedPreferences.edit().apply {
                    putString(
                        getString(R.string.user_bearer_token_key),
                        ""
                    )
                }.apply()
            }
        }
    }
}
