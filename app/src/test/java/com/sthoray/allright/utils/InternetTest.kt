package com.sthoray.allright.utils

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class InternetTest {
    @RelaxedMockK
    lateinit var app: Application

    @RelaxedMockK
    lateinit var connectivityManager: ConnectivityManager

    @RelaxedMockK
    lateinit var activeNetwork: Network

    @RelaxedMockK
    lateinit var capabilities: NetworkCapabilities

    val CONNECTIVITY_SERVICE = "connectivity"


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Constants)
    }

    @Test
    fun getInternet_API_above_or_equal_to_M_success_returns_true() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { Constants.getVersionSDKInt() } returns 23
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns capabilities

        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true

        val hasConnection = Internet.hasConnection(app)

        verify {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        }
        verify(exactly = 0){
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
        assertThat(hasConnection).isTrue()
    }
}