package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import com.sthoray.allright.data.repository.AppRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * MainViewModel test
 */
class MainViewModelTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private val application = mockk<Application>()
    private val appRepository = mockk<AppRepository>()
    private val mainViewModel: MainViewModel = MainViewModel(application, appRepository)
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getFeatureCategories() {

    }

    @Test
    fun getTopLevelCategories() {
    }
}