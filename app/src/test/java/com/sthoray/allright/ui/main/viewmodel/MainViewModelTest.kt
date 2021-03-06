package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.listing.CategorySmall
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
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
    private lateinit var secondTierCategoriesResponse: List<CategorySmall>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }

    @Test
    fun getFeaturedCategories_withResponseSuccess_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(app) } returns true
            coEvery {
                appRepository.getFeatureCategories()
            } returns Response.success(featureCategoriesResponse)

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Success::class.java)
            assertThat(mainViewModel.featureCategories.value?.data)
                .isEqualTo(featureCategoriesResponse)
        }

    @Test
    fun getFeaturedCategories_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorResponse: Response<FeatureCategoriesResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getFeatureCategories() } returns errorResponse

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo(errorResponse.message())
        }

    @Test
    fun getFeaturedCategories_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns false
            every { app.getString(any()) } returns errorMsg

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }
            coVerify(exactly = 0) { appRepository.getFeatureCategories() }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo(errorMsg)
        }

    @Test
    fun getFeaturedCategories_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns true
            every { app.getString(any()) } returns errorMsg
            coEvery { appRepository.getFeatureCategories() } throws IOException()

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo(errorMsg)
        }

    @Test
    fun getFeaturedCategories_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns true
            every { app.getString(any()) } returns errorMsg
            coEvery {
                appRepository.getFeatureCategories()
            } throws JsonSyntaxException("Mockk exception message")

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo(errorMsg)
        }

    @Test
    fun getSecondTierCategories_withResponseSuccess_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(app) } returns true
            coEvery {
                appRepository.getSecondTierCategories()
            } returns Response.success(secondTierCategoriesResponse)

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.secondTierCategories.value)
                .isInstanceOf(Resource.Success::class.java)
            assertThat(mainViewModel.secondTierCategories.value?.data)
                .isEqualTo(secondTierCategoriesResponse)
        }

    @Test
    fun getSecondTierCategories_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorResponse: Response<List<CategorySmall>> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getSecondTierCategories() } returns errorResponse

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.secondTierCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.secondTierCategories.value?.message)
                .isEqualTo(errorResponse.message())
        }

    @Test
    fun getSecondTierCategories_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns false
            every { app.getString(any()) } returns errorMsg

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }
            coVerify(exactly = 0) { appRepository.getSecondTierCategories() }

            assertThat(mainViewModel.secondTierCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.secondTierCategories.value?.message)
                .isEqualTo(errorMsg)
        }

    @Test
    fun getSecondTierCategories_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns true
            every { app.getString(any()) } returns errorMsg
            coEvery { appRepository.getSecondTierCategories() } throws IOException()

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.secondTierCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.secondTierCategories.value?.message)
                .isEqualTo(errorMsg)
        }

    @Test
    fun getSecondTierCategories_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorMsg = "Some error message"

            every { Internet.hasConnection(any()) } returns true
            every { app.getString(any()) } returns errorMsg
            coEvery {
                appRepository.getSecondTierCategories()
            } throws JsonSyntaxException("Mockk exception message")

            val mainViewModel = MainViewModel(app, appRepository)

            verify { Internet.hasConnection(any()) }

            assertThat(mainViewModel.featureCategories.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(mainViewModel.featureCategories.value?.message)
                .isEqualTo(errorMsg)
        }
}
