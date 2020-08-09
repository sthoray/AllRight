package com.sthoray.allright.utils

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build.VERSION_CODES.*
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class InternetTest {

    @RelaxedMockK
    lateinit var app: Application

    @RelaxedMockK
    lateinit var connectivityManager: ConnectivityManager

    @RelaxedMockK
    lateinit var activeNetwork: Network

    @Suppress("DEPRECATION")
    @RelaxedMockK
    lateinit var activeNetworkInfo: NetworkInfo

    @RelaxedMockK
    lateinit var capabilities: NetworkCapabilities

    private val CONNECTIVITY_SERVICE = "connectivity"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Suppress("DEPRECATION")
    @Config(minSdk = M, maxSdk = P)
    @Test
    fun getInternet_API_at_least_M_success_returns_true() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns capabilities

        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true

        val hasConnection = Internet.hasConnection(app)

        verify {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
        verify(exactly = 0) {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            connectivityManager.activeNetworkInfo

        }
        assertThat(hasConnection).isTrue()
    }

    @Suppress("DEPRECATION")
    @Config(minSdk = M, maxSdk = P)
    @Test
    fun getInternet_API_at_least_M_no_wifi_or_cellular_or_ethernet_returns_false() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns capabilities

        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns false

        val hasConnection = Internet.hasConnection(app)

        verifySequence {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
        verify(exactly = 0) {
            connectivityManager.activeNetworkInfo
        }
        assertThat(hasConnection).isFalse()
    }

    @Suppress("DEPRECATION")
    @Config(minSdk = M, maxSdk = P)
    @Test
    fun getInternet_API_at_least_M_when_activeNetwork_returns_null_returns_false() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns null
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns capabilities

        val hasConnection = Internet.hasConnection(app)
        verify(exactly = 0) {
            connectivityManager.getNetworkCapabilities(activeNetwork)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            connectivityManager.activeNetworkInfo

        }
        assertThat(hasConnection).isFalse()
    }

    @Suppress("DEPRECATION")
    @Config(minSdk = M, maxSdk = P)
    @Test
    fun getInternet_API_at_least_M_when_getNetworkCapabilities_returns_null_returns_false() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns null
        val hasConnection = Internet.hasConnection(app)
        verify(exactly = 0) {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            connectivityManager.activeNetworkInfo

        }
        assertThat(hasConnection).isFalse()
    }

    @Suppress("DEPRECATION")
    @Config(maxSdk = LOLLIPOP_MR1)
    @Test
    fun getInternet_API_less_than_M_success_returns_true() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetworkInfo } returns activeNetworkInfo
        every {
            connectivityManager.activeNetworkInfo?.type
        } answers { NetworkCapabilities.TRANSPORT_WIFI }
        val hasConnection = Internet.hasConnection(app)

        verify {
            connectivityManager.activeNetworkInfo
        }
        assertThat(connectivityManager.activeNetworkInfo?.type).isEqualTo(NetworkCapabilities.TRANSPORT_WIFI)

        assertThat(hasConnection).isTrue()
    }

    @Suppress("DEPRECATION")
    @Config(maxSdk = LOLLIPOP_MR1)
    @Test
    fun getInternet_API_less_than_M_failure_returns_false() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager

        every { connectivityManager.activeNetworkInfo } returns activeNetworkInfo
        every {
            connectivityManager.activeNetworkInfo?.type
        } answers { ConnectivityManager.TYPE_DUMMY }
        val hasConnection = Internet.hasConnection(app)

        verify {
            connectivityManager.activeNetworkInfo
            ConnectivityManager.TYPE_WIFI
            ConnectivityManager.TYPE_MOBILE
            ConnectivityManager.TYPE_ETHERNET
        }

        assertThat(hasConnection).isFalse()
    }

    @Suppress("DEPRECATION")
    @Config(maxSdk = LOLLIPOP_MR1)
    @Test
    fun getInternet_API_less_than_M_failure_because_active_network_info_is_null_returns_false() {
        every { app.getSystemService(CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetworkInfo } returns null
        val hasConnection = Internet.hasConnection(app)

        verify {
            connectivityManager.activeNetworkInfo
        }
        assertThat(hasConnection).isFalse()
    }
}