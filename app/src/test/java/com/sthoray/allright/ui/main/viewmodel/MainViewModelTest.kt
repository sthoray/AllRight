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

class MainViewModelTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private val application = mockk<Application>()
    private val appRepository = mockk<AppRepository>()
    private lateinit var mainViewModel: MainViewModel
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mainViewModel = MainViewModel(application, appRepository)

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