package com.sthoray.allright.ui.listing.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
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
    fun getListing_withSuccessfulResponse_setsResourceSuccess() =
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
}