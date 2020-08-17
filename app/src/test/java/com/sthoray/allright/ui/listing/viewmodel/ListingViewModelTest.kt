package com.sthoray.allright.ui.listing.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.util.Internet
import com.sthoray.allright.util.Resource
import com.sthoray.allright.util.TestCoroutineRule
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
class ListingViewModelTest {

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

    @Test
    fun getListing_withResponseSuccess_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            val testId = 9

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getListing(eq(testId)) } returns Response.success(listing)

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }

            assertThat(listingViewModel.listing.value).isInstanceOf(Resource.Success::class.java)
            assertThat(listingViewModel.listing.value?.data).isEqualTo(listing)
        }

    @Test
    fun getListing_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testId = 9
            val errorResponse: Response<Listing> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getListing(eq(testId)) } returns errorResponse

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }

            assertThat(listingViewModel.listing.value).isInstanceOf(Resource.Error::class.java)
            assertThat(listingViewModel.listing.value?.message).isEqualTo(errorResponse.message())
        }

    @Test
    fun getListing_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testId = 9

            every { Internet.hasConnection(any()) } returns false

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }
            coVerify(exactly = 0) { appRepository.getListing(any()) }

            assertThat(listingViewModel.listing.value).isInstanceOf(Resource.Error::class.java)
            assertThat(listingViewModel.listing.value?.message)
                .isEqualTo("No internet connection")
        }

    @Test
    fun getListing_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testId = 9

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getListing(eq(testId)) } throws IOException()

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }

            assertThat(listingViewModel.listing.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(listingViewModel.listing.value?.message)
                .isEqualTo("Network Failure")
        }

    @Test
    fun getListing_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testId = 9

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.getListing(eq(testId))
            } throws JsonSyntaxException("Mockk exception message")

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }

            assertThat(listingViewModel.listing.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(listingViewModel.listing.value?.message)
                .isEqualTo("Conversion Error")
        }
}