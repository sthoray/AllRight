package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.listing.Category
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
import retrofit2.Response
import java.io.IOException

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

    @RelaxedMockK
    private lateinit var topLevelCategoriesResponse: List<Category>

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
            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getFeatureCategories() } throws IOException()
            val mainViewModel = MainViewModel(app, appRepository)
            verify { Internet.hasConnection(any()) }
            coVerify(exactly = 0) { appRepository.getFeatureCategories() }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo("No internet connection")
//        assertThat(mainViewModel.featureCategories.value?.data)
//            .isEqualTo(featureCategoriesResponse)
    }
    /**
     * Tests that a "Network Failure" error resource is created when
     * the AppRepository
     */
    @Test
    fun getFeaturedCategoriesErrorNetworkFailure() {
        every { Internet.hasConnection(any()) } returns true
        coEvery { appRepository.getFeatureCategories() } throws IOException()

        val mainViewModel = MainViewModel(app, appRepository)

        verify { Internet.hasConnection(any()) }

        assertThat(mainViewModel.featureCategories.value)
            .isInstanceOf(Resource.Error::class.java)
        assertThat(mainViewModel.featureCategories.value?.message)
            .isEqualTo("Network Failure")
    }
    /**
     *
     */
    @Test
    fun getFeaturedCategoriesErrorConversionFailure(){
        every { Internet.hasConnection(any()) } returns true
        coEvery {
            appRepository.getSecondTierCategories()
        } throws JsonSyntaxException("Mockk exception message")

        val mainViewModel = MainViewModel(app, appRepository)


        verify { Internet.hasConnection(any()) }

        assertThat(mainViewModel.topLevelCategories.value)
            .isInstanceOf(Resource.Error::class.java)
        assertThat(mainViewModel.topLevelCategories.value?.message)
            .isEqualTo("Conversion Error")
    }

    /**
     * Tests that featured categories correctly
     * posts the correct response to the feature categories LiveData
     */
    @Test
    fun getTopLevelCategoriesSuccessful(){
        every { Internet.hasConnection(app) } returns true
        coEvery {
            appRepository.getSecondTierCategories()
        } returns Response.success(topLevelCategoriesResponse)

        val mainViewModel = MainViewModel(app, appRepository)

        assertThat(mainViewModel.topLevelCategories.value)
            .isInstanceOf(Resource.Success::class.java)
        assertThat(mainViewModel.topLevelCategories.value?.data)
            .isEqualTo(topLevelCategoriesResponse)
    }

    /**
     * Tests that a "No internet connection" error resource is created when
     * the user does not have an internet connection.
     */
    @Test
    fun getTopLevelCategoriesErrorInternet() {
        every { Internet.hasConnection(any()) } returns true
        coEvery { appRepository.getSecondTierCategories() } throws IOException()
        val mainViewModel = MainViewModel(app, appRepository)
        verify { Internet.hasConnection(any()) }
        coVerify(exactly = 0) { appRepository.getSecondTierCategories() }

        assertThat(mainViewModel.topLevelCategories.value)
            .isInstanceOf(Resource.Error::class.java)
        assertThat(mainViewModel.topLevelCategories.value?.message)
            .isEqualTo("No internet connection")
//        assertThat(mainViewModel.featureCategories.value?.data)
//            .isEqualTo(featureCategoriesResponse)
    }
    /**
     * Tests that a "Network Failure" error resource is created when
     * the AppRepository
     */
    @Test
    fun getTopLevelCategoriesErrorNetworkFailure() {
        every { Internet.hasConnection(any()) } returns true
        coEvery { appRepository.getSecondTierCategories() } throws IOException()

        val mainViewModel = MainViewModel(app, appRepository)

        verify { Internet.hasConnection(any()) }

        assertThat(mainViewModel.topLevelCategories.value)
            .isInstanceOf(Resource.Error::class.java)
        assertThat(mainViewModel.topLevelCategories.value?.message)
            .isEqualTo("Network Failure")
    }
    /**
     *
     */
    @Test
    fun getTopLevelCategoriesErrorConversionFailure(){
        every { Internet.hasConnection(any()) } returns true
        coEvery {
            appRepository.getSecondTierCategories()
        } throws JsonSyntaxException("Mockk exception message")

        val mainViewModel = MainViewModel(app, appRepository)


        verify { Internet.hasConnection(any()) }

        assertThat(mainViewModel.topLevelCategories.value)
            .isInstanceOf(Resource.Error::class.java)
        assertThat(mainViewModel.topLevelCategories.value?.message)
            .isEqualTo("Conversion Error")
    }
}
