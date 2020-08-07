package com.sthoray.allright.ui.search.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.search.Location
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
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
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response
import java.io.IOException

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
    private var searchListingsResponse: SearchResponse? = null

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }
    @Test
    fun searchListingsSuccessfulNullResponseSetsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(searchRequest)
            } returns Response.success(searchListingsResponse)

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Success::class.java)
            assertThat(searchViewModel.searchListings.value?.data)
                .isEqualTo(searchListingsResponse)
        }


    @Test
    fun searchListingsErrorInternet() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns false
            coEvery {
                appRepository.searchListings(searchRequest)
            } returns Response.success(searchListingsResponse)

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(searchViewModel.searchListings.value?.message)
                .isEqualTo("No internet connection")
        }
    @Test
    fun searchListingsErrorNetworkFailure() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(searchRequest)
            } throws IOException()

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(searchViewModel.searchListings.value?.message)
                .isEqualTo("Network Failure")
        }
    @Test
    fun searchListingsErrorConversion() =
        mainCoroutineRule.runBlockingTest {
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(searchRequest)
            } throws JsonSyntaxException("Mockk Exception Message")

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(searchViewModel.searchListings.value?.message)
                .isEqualTo("Conversion Error")
        }
    @Test
    fun getListingsErrorSetsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val errorResponse: Response<SearchResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.searchListings(searchRequest) } returns errorResponse

            val searchViewModel = SearchViewModel(app, appRepository)
            searchViewModel.searchRequest = searchRequest
            searchViewModel.searchListings()
            verify { Internet.hasConnection(any()) }

            assertThat(searchViewModel.searchListings.value)
                .isInstanceOf(Resource.Error::class.java)
            assertThat(searchViewModel.searchListings.value?.message)
                .isEqualTo(errorResponse.message())
        }
    @Test
    fun setDraftBinaryFilters() {
        val searchViewModel = SearchViewModel(app, appRepository)
        searchViewModel.searchRequestDraft = SearchRequest()
        searchViewModel.setDraftBinaryFilters(
            freeShipping = true,
            fastShipping = true,
            brandNew = true
        )
        draftSearchRequest = SearchRequest(freeShipping = 1, fastShipping = 1, brandNew = 1)

        assertThat(searchViewModel.searchRequestDraft).isEqualTo(draftSearchRequest)
    }

    @After
    fun tearDown() {
    }
}