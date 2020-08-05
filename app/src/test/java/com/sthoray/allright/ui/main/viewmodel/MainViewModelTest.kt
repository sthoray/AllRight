package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var app: Application

    @MockK
    private lateinit var appRepository: AppRepository

    @RelaxedMockK
    private lateinit var listing: Listing

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }
}