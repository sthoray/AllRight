package com.sthoray.allright.ui.search.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var app: Application

    @MockK
    private lateinit var appRepository: AppRepository

    @RelaxedMockK
    private lateinit var searchRequest: SearchRequest

    @RelaxedMockK
    private lateinit var draftSearchRequest: SearchRequest

    @RelaxedMockK
    private lateinit var searchListing: SearchResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }
    @Test
    fun searchListingsSuccessfulSetsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(searchRequest)
            } returns Response.success(searchListing)

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Success::class.java)
            assertThat(searchViewModel.searchListings.value?.data)
                .isEqualTo(searchListing)
        }
    @Test
    fun searchListingsErrorInternet() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns false
            coEvery {
                appRepository.searchListings(searchRequest)
            } returns Response.success(searchListing)

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(searchViewModel.searchListings.value?.message)
                .isEqualTo("No internet connection")
        }
    @After
    fun tearDown() {
    }
}