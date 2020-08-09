package com.sthoray.allright.utils

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
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
    }

    @Test
    fun getInternet_API_above_M_success_returns_true() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns capabilities

        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true

        val hasConnection = Internet.hasConnection(app)

        assertThat(hasConnection).isTrue()
    }
}