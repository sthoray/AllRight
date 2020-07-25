package com.sthoray.allright.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Internet utils class
 */
class Internet {
    companion object {
        /**
         * Checks if the application has a connection to the Internet
         *@param application The current application state
         *@return whether the application has a connection to the Internet
         */
        fun hasConnection(application: Application): Boolean {
            val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager
                    .getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }
    }
}