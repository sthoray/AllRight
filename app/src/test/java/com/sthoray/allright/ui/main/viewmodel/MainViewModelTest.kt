package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.test.core.app.ActivityScenario.launch
import com.sthoray.allright.data.repository.AppRepository
import io.mockk.mockk
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class MainViewModelTest {
    private val application = mockk<Application>()
    private val appRepository = mockk<AppRepository>()
    private val coroutineDispatcherImplementation = object : CoroutineDispatcher(){
        override fun dispatch(context: CoroutineContext, block: Runnable) {

        }


    }
    private lateinit var mainViewModel: MainViewModel
    @Before
    fun setUp() {
        mainViewModel = MainViewModel(application, appRepository)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getFeatureCategories() {

    }

    @Test
    fun getTopLevelCategories() {
    }
}