package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.repository.AppRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

/**
 * MainViewModel test
 */
class MainViewModelTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private val application = mockk<Application>()
    private val appRepository = mockk<AppRepository>()
    private val mainViewModel: MainViewModel = MainViewModel(application, appRepository)

    /**
     * Clearing the mocks to ensure that each test is stateless
     */
    @BeforeEach
    fun init(){
        clearMocks(appRepository, application)
    }


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

    /**
     * The init function of MainViewModel should call
     * getFeaturedCategories() and getSecondTierCategories()
     * This test verifies that the getFeaturedCategories() is called from init
     */
    @Test
    fun `verify getFeaturedCategories() called from init`(){
    verify { mainViewModel.getFeaturedCategories() }
    }
    /**
     * The init function of MainViewModel should call
     * getFeaturedCategories() and getSecondTierCategories()
     * This test verifies that the getSecondTierCategories() is called from init
     */
    @Test
    fun `verify getSecondTierCategories() called from init`(){
        verify { mainViewModel.getSecondTierCategories() }

    }
    @Test
    fun getFeatureCategories() {

    }

    @Test
    fun getSecondTierCategories() {

    }
}