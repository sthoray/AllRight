package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import com.sthoray.allright.data.repository.AppRepository
import io.mockk.clearMocks
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach

/**
 * MainViewModel test
 */
@ExperimentalCoroutinesApi
class MainViewModelTest {
    private val application = mockk<Application>()
    private val appRepository = mockk<AppRepository>()
    private val mainViewModel: MainViewModel = MainViewModel(application, appRepository)

    /**
     * Clearing the mocks to ensure that each test is stateless
     */
    @BeforeEach
    fun init() {
        clearMocks(appRepository, application)
    }

    /**
     * Dispatchers.Main not available in unit testing
     */
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {

    }

    /**
     * Clean up Dispatchers.setMain()
     */
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {

    }



}