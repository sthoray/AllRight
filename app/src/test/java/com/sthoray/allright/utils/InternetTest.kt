package com.sthoray.allright.utils

import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import kotlin.properties.Delegates

class InternetTest {
    @RelaxedMockK
    var deviceApi by Delegates.notNull<Int>()

    @Before
    fun setUp() {
    }
}