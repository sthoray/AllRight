package com.sthoray.allright.ui.search.result.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.model.search.SearchResponseMetadata
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
class SearchViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var app: Application

    @MockK
    private lateinit var appRepository: AppRepository

    private val testId = 99

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }

    @Test
    fun initSearch_withUninitialisedSearchRequest_initialisesSearchRequest() =
        mainCoroutineRule.runBlockingTest {
            SearchViewModel(
                app,
                appRepository
            ).apply {
                initSearch(testId)

                coVerify { searchListings() }

                assertThat(searchRequest.categoryId).isEqualTo(testId)
                assertThat(searchRequestDraft).isEqualTo(searchRequest)
            }
        }

    @Test
    fun initSearch_withInitialisedSearchRequest_doesNothing() = mainCoroutineRule.runBlockingTest {
        val testSearchRequest = SearchRequest(categoryId = testId)

        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequest = testSearchRequest
            initSearch(testId + 1)

            coVerify(exactly = 0) { searchListings() }

            assertThat(searchRequest).isEqualTo(testSearchRequest)
        }
    }

    @Test
    fun searchListings_withResponseSuccess_and_firstPage_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val testSearchResponse = mockk<SearchResponse>(relaxed = true)

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } returns Response.success(testSearchResponse)

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListings()

                verify { Internet.hasConnection(any()) }

                assertThat(searchRequest.pageNumber).isEqualTo(2)
                assertThat(searchListings.value).isInstanceOf(Resource.Success::class.java)
                assertThat(searchListings.value?.data).isEqualTo(testSearchResponse)
            }
        }

    @Test
    fun searchListings_withResponseSuccess_and_notFirstPage_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            val startingPage = 2
            val testSearchRequest = SearchRequest(categoryId = testId, pageNumber = startingPage)
            val singleSearchListingResponse = SearchResponse(
                mutableListOf(mockk<Listing>(relaxed = true)),
                mockk<SearchResponseMetadata>(relaxed = true)
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } returns Response.success(singleSearchListingResponse)

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListingsResponse = singleSearchListingResponse
                searchListings()

                verify { Internet.hasConnection(any()) }

                assertThat(searchRequest.pageNumber).isEqualTo(startingPage + 1)
                assertThat(searchListings.value).isInstanceOf(Resource.Success::class.java)
                assertThat(searchListings.value?.data?.data?.size).isEqualTo(2)
            }
        }

    @Test
    fun searchListings_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorResponse: Response<SearchResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.searchListings(eq(testSearchRequest)) } returns errorResponse

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListings()

                verify { Internet.hasConnection(any()) }

                assertThat(searchListings.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchListings.value?.message).isEqualTo(errorResponse.message())
            }
        }

    @Test
    fun searchListings_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)

            every { Internet.hasConnection(any()) } returns false

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListings()

                verify { Internet.hasConnection(any()) }
                coVerify(exactly = 0) { appRepository.searchListings(any()) }

                assertThat(searchListings.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchListings.value?.message).isEqualTo("No internet connection")
            }
        }

    @Test
    fun searchListings_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws IOException()

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListings()

                verify { Internet.hasConnection(any()) }

                assertThat(searchListings.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchListings.value?.message).isEqualTo("Network Failure")
            }
        }

    @Test
    fun searchListings_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws JsonSyntaxException("Something happened")

            SearchViewModel(
                app,
                appRepository
            ).apply {
                searchRequest = testSearchRequest
                searchListings()

                verify { Internet.hasConnection(any()) }

                assertThat(searchListings.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchListings.value?.message).isEqualTo("Conversion Error")
            }
        }

    @Test
    fun applyFiltersAndSearch() {
        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequestDraft = SearchRequest(pageNumber = 999)
            searchRequest = mockk(relaxed = true)
            searchListingsResponse = mockk(relaxed = true)

            applyFiltersAndSearch()

            verify { searchListings() }

            assertThat(searchRequest).isEqualTo(SearchRequest(pageNumber = 1))
            assertThat(searchRequest.pageNumber).isEqualTo(1)
        }
    }

    @Test
    fun setDraftBinaryFilters_withTrueParameters_isCorrect() {
        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequestDraft = SearchRequest()
            setDraftBinaryFilters(
                freeShipping = true,
                fastShipping = true,
                brandNew = true
            )
            assertThat(searchRequestDraft.freeShipping).isEqualTo(1)
            assertThat(searchRequestDraft.fastShipping).isEqualTo(1)
            assertThat(searchRequestDraft.brandNew).isEqualTo(1)
        }
    }

    @Test
    fun setDraftBinaryFilters_withFalseParameters_isCorrect() {
        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequestDraft = SearchRequest()
            setDraftBinaryFilters(
                freeShipping = false,
                fastShipping = false,
                brandNew = false
            )
            assertThat(searchRequestDraft.freeShipping).isEqualTo(0)
            assertThat(searchRequestDraft.fastShipping).isEqualTo(0)
            assertThat(searchRequestDraft.brandNew).isEqualTo(0)
        }
    }

    @Test
    fun setDraftMarketplace_asMall_isCorrect() {
        val initialSearchRequest = SearchRequest()
        val expectedSearchRequest = SearchRequest(auctions = 0, products = 1)

        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequestDraft = initialSearchRequest
            setDraftMarketplace(isMall = true)

            assertThat(searchRequestDraft).isEqualTo(expectedSearchRequest)
        }
    }

    @Test
    fun setDraftMarketplace_asSecondhand_isCorrect() {
        val initialSearchRequest = SearchRequest()
        val expectedSearchRequest = SearchRequest(auctions = 1, products = 0)

        SearchViewModel(
            app,
            appRepository
        ).apply {
            searchRequestDraft = initialSearchRequest
            setDraftMarketplace(isMall = false)

            assertThat(searchRequestDraft).isEqualTo(expectedSearchRequest)
        }
    }

    @Test
    fun isMall_withMallRequest_returnsTrue() {
        val mallSearchRequest = SearchRequest(auctions = 0, products = 1)

        SearchViewModel(
            app,
            appRepository
        ).apply {
            assertThat(isMall(mallSearchRequest)).isTrue()
        }
    }

    @Test
    fun isMall_withSecondhandRequest_returnsFalse() {
        val secondhandSearchRequest = SearchRequest(auctions = 1, products = 0)

        SearchViewModel(
            app,
            appRepository
        ).apply {
            assertThat(isMall(secondhandSearchRequest)).isFalse()
        }
    }
}