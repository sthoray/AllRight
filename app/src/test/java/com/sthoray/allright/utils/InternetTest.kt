package com.sthoray.allright.utils

import android.app.Application
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class InternetTest {
    @RelaxedMockK
    lateinit var app : Application
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getInternet_API_above_M_success_returns_true(){

    }
}