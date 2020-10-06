package com.sthoray.allright.ui.base

import android.app.Application
import com.sthoray.allright.BuildConfig
import timber.log.Timber

/**
 * The main application class used to initialise Timber.
 *
 * Since the application class will be active for the entire lifetime of the
 * application, it is the best place to initialise the Timber library for
 * easy logging.
 */
class MainApplication : Application() {

    /**
     * Create this application class and initialise Timber.
     *
     * This will disable logging under RELEASE app variants.
     */
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}