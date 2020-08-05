package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.any
import retrofit2.Response

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
    private lateinit var featureCategoriesResponse: FeatureCategoriesResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }

    /**
     * Tests that featured categories correctly
     * posts the correct response to the feature categories LiveData
     */
    @Test
    fun getFeaturedCategoriesSuccessful(){
        every { Internet.hasConnection(app) } returns true
        coEvery {
            appRepository.getFeatureCategories()
        } returns Response.success(featureCategoriesResponse)

        val mainViewModel = MainViewModel(app, appRepository)

        assertThat(mainViewModel.featureCategories.value)
            .isInstanceOf(Resource.Success::class.java)
        assertThat(mainViewModel.featureCategories.value?.data)
            .isEqualTo(featureCategoriesResponse)
    }

    /**
     * Tests that a "No internet connection" error resource is created when
     * the user does not have an internet connection.
     */
    @Test
    fun getFeaturedCategoriesErrorInternet() {
        every { Internet.hasConnection(any()) } returns false

        val mainViewModel = MainViewModel(app, appRepository)
        verify { Internet.hasConnection(any()) }
        coVerify(exactly = 0){ appRepository.getFeatureCategories() }

        assertThat(mainViewModel.featureCategories.value)
            .isInstanceOf(Resource.Error::class.java)
//        assertThat(mainViewModel.featureCategories.value?.data)
//            .isEqualTo(featureCategoriesResponse)
    }
}