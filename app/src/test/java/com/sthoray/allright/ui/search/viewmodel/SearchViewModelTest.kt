package com.sthoray.allright.ui.search.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import com.sthoray.allright.data.model.browse.BrowseResponse
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.model.search.SearchResponseMetadata
import com.sthoray.allright.data.repository.AppRepository
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
    fun initSearchMall_withUninitialisedSearchRequest_initialisesSearchRequest() =
        mainCoroutineRule.runBlockingTest {
            every { app.getString(any()) } returns ""

            SearchViewModel(app, appRepository).apply {
                initSearch(testId, "mall")

                assertThat(searchRequest.categoryId).isEqualTo(testId)
                assertThat(searchRequest.isMall()).isTrue()
                assertThat(searchRequestDraft).isSameInstanceAs(searchRequest)
            }
        }

    @Test
    fun initSearchSecondhand_withUninitialisedSearchRequest_initialisesSearchRequest() =
        mainCoroutineRule.runBlockingTest {
            every { app.getString(any()) } returns ""

            SearchViewModel(app, appRepository).apply {
                initSearch(testId, "secondhand")

                assertThat(searchRequest.categoryId).isEqualTo(testId)
                assertThat(searchRequest.isMall()).isFalse()
                assertThat(searchRequestDraft).isSameInstanceAs(searchRequest)
            }
        }

    @Test
    fun initSearch_withInitialisedSearchRequest_doesNothing() = mainCoroutineRule.runBlockingTest {
        val testSearchRequest = SearchRequest(categoryId = testId)

        SearchViewModel(app, appRepository).apply {
            searchRequest = testSearchRequest
            initSearch(testId + 1, "mall")

            coVerify(exactly = 0) { search() }

            assertThat(searchRequest).isEqualTo(testSearchRequest)
        }
    }

    @Test
    fun search_withResponseSuccess_and_firstPage_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val testSearchResponse = mockk<SearchResponse>(relaxed = true)
            val testBrowseResponse = mockk<BrowseResponse>(relaxed = true)

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } returns Response.success(testSearchResponse)
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } returns Response.success(testBrowseResponse)

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                search()

                verify { Internet.hasConnection(any()) }

                assertThat(searchRequest.pageNumber).isEqualTo(2)
                assertThat(searchResponse.value).isInstanceOf(Resource.Success::class.java)
                assertThat(searchResponse.value?.data).isEqualTo(testSearchResponse)
                assertThat(browseResponse.value).isInstanceOf(Resource.Success::class.java)
                assertThat(browseResponse.value?.data).isEqualTo(testBrowseResponse)
            }
        }

    @Test
    fun search_withResponseSuccess_and_notFirstPage_setsResourceSuccess() =
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

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                lastSearchResponse = singleSearchListingResponse
                search()

                verify { Internet.hasConnection(any()) }

                assertThat(searchRequest.pageNumber).isEqualTo(startingPage + 1)
                assertThat(searchResponse.value).isInstanceOf(Resource.Success::class.java)
                assertThat(searchResponse.value?.data?.data?.size).isEqualTo(2)
            }
        }

    @Test
    fun search_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorResponse: Response<SearchResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            val browseErrorResponse: Response<BrowseResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.searchListings(eq(testSearchRequest)) } returns errorResponse
            coEvery { appRepository.browseCategory(eq(testId), eq(1)) } returns browseErrorResponse

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                search()

                verify { Internet.hasConnection(any()) }

                assertThat(searchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchResponse.value?.message).isEqualTo(errorResponse.message())
                assertThat(browseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(browseResponse.value?.message).isEqualTo(browseErrorResponse.message())
            }
        }

    @Test
    fun search_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns false

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                search()

                verify { Internet.hasConnection(any()) }
                coVerify(exactly = 0) { appRepository.searchListings(any()) }
                coVerify(exactly = 0) { appRepository.browseCategory(any(), any()) }

                assertThat(searchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(browseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(browseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun search_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws IOException()
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } throws IOException()

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                search()

                verify { Internet.hasConnection(any()) }

                assertThat(searchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(browseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(browseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun search_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws JsonSyntaxException("Something happened")
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } throws JsonSyntaxException("Something happened")

            SearchViewModel(app, appRepository).apply {
                searchRequest = testSearchRequest
                search()

                verify { Internet.hasConnection(any()) }

                assertThat(searchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(searchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(browseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(browseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun draftSearch_withResponseSuccess_setsResourceSuccess() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId, pageNumber = 50)
            val testSearchResponse = mockk<SearchResponse>(relaxed = true)
            val testBrowseResponse = mockk<BrowseResponse>(relaxed = true)

            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } returns Response.success(testSearchResponse)
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } returns Response.success(testBrowseResponse)

            SearchViewModel(app, appRepository).apply {
                searchRequestDraft = testSearchRequest
                draftSearch()

                verify { Internet.hasConnection(any()) }

                assertThat(searchRequestDraft.pageNumber).isEqualTo(1)
                assertThat(draftSearchResponse.value).isInstanceOf(Resource.Success::class.java)
                assertThat(draftSearchResponse.value?.data).isEqualTo(testSearchResponse)
                assertThat(draftBrowseResponse.value).isInstanceOf(Resource.Success::class.java)
                assertThat(draftBrowseResponse.value?.data).isEqualTo(testBrowseResponse)
            }
        }

    @Test
    fun draftSearch_withResponseError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorResponse: Response<SearchResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            val browseErrorResponse: Response<BrowseResponse> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.searchListings(eq(testSearchRequest)) } returns errorResponse
            coEvery { appRepository.browseCategory(eq(testId), eq(1)) } returns browseErrorResponse

            SearchViewModel(app, appRepository).apply {
                searchRequestDraft = testSearchRequest
                draftSearch()

                verify { Internet.hasConnection(any()) }

                assertThat(draftSearchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftSearchResponse.value?.message).isEqualTo(errorResponse.message())
                assertThat(draftBrowseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftBrowseResponse.value?.message).isEqualTo(browseErrorResponse.message())
            }
        }

    @Test
    fun draftSearch_withoutNetworkConnection_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns false

            SearchViewModel(app, appRepository).apply {
                searchRequestDraft = testSearchRequest
                draftSearch()

                verify { Internet.hasConnection(any()) }
                coVerify(exactly = 0) { appRepository.searchListings(any()) }
                coVerify(exactly = 0) { appRepository.browseCategory(any(), any()) }

                assertThat(draftSearchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftSearchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(draftBrowseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftBrowseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun draftSearch_withNetworkFailure_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws IOException()
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } throws IOException()

            SearchViewModel(app, appRepository).apply {
                searchRequestDraft = testSearchRequest
                draftSearch()

                verify { Internet.hasConnection(any()) }

                assertThat(draftSearchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftSearchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(draftBrowseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftBrowseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun draftSearch_withConversionError_setsResourceError() =
        mainCoroutineRule.runBlockingTest {
            val testSearchRequest = SearchRequest(categoryId = testId)
            val errorMsg = "Some error message"

            every { app.getString(any()) } returns errorMsg
            every { Internet.hasConnection(any()) } returns true
            coEvery {
                appRepository.searchListings(eq(testSearchRequest))
            } throws JsonSyntaxException("Something happened")
            coEvery {
                appRepository.browseCategory(eq(testId), eq(1))
            } throws JsonSyntaxException("Something happened")

            SearchViewModel(app, appRepository).apply {
                searchRequestDraft = testSearchRequest
                draftSearch()

                verify { Internet.hasConnection(any()) }

                assertThat(draftSearchResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftSearchResponse.value?.message).isEqualTo(errorMsg)
                assertThat(draftBrowseResponse.value).isInstanceOf(Resource.Error::class.java)
                assertThat(draftBrowseResponse.value?.message).isEqualTo(errorMsg)
            }
        }

    @Test
    fun initFilters_setsDraftSearchRequest() {
        SearchViewModel(app, appRepository).apply {
            searchRequest = SearchRequest(categoryId = testId)
            initFilters()
            assertThat(searchRequestDraft).isNotSameInstanceAs(searchRequest)
            assertThat(searchRequestDraft).isEqualTo(searchRequest)
        }
    }

    @Test
    fun applyFiltersAndSearch() {
        SearchViewModel(app, appRepository).apply {
            searchRequestDraft = SearchRequest(pageNumber = 999)
            searchRequest = mockk(relaxed = true)
            lastSearchResponse = mockk(relaxed = true)

            every { app.getString(any()) } returns ""

            applyFiltersAndSearch()

            assertThat(searchRequest).isEqualTo(SearchRequest(pageNumber = 1))
            assertThat(searchRequest.pageNumber).isEqualTo(1)
        }
    }

    @Test
    fun setDraftBinaryFilters_withTrueParameters_isCorrect() {
        SearchViewModel(app, appRepository).apply {
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
        SearchViewModel(app, appRepository).apply {
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

        SearchViewModel(app, appRepository).apply {
            searchRequestDraft = initialSearchRequest
            setDraftMarketplace(isMall = true)

            assertThat(searchRequestDraft).isEqualTo(expectedSearchRequest)
        }
    }

    @Test
    fun setDraftMarketplace_asSecondhand_isCorrect() {
        val initialSearchRequest = SearchRequest()
        val expectedSearchRequest = SearchRequest(auctions = 1, products = 0)

        SearchViewModel(app, appRepository).apply {
            searchRequestDraft = initialSearchRequest
            setDraftMarketplace(isMall = false)

            assertThat(searchRequestDraft).isEqualTo(expectedSearchRequest)
        }
    }

    @Test
    fun isMall_withMallRequest_returnsTrue() {
        val mallSearchRequest = SearchRequest(auctions = 0, products = 1)

        SearchViewModel(app, appRepository).apply {
            assertThat(mallSearchRequest.isMall()).isTrue()
        }
    }

    @Test
    fun isMall_withSecondhandRequest_returnsFalse() {
        val secondhandSearchRequest = SearchRequest(auctions = 1, products = 0)

        SearchViewModel(app, appRepository).apply {
            assertThat(secondhandSearchRequest.isMall()).isFalse()
        }
    }

    @Test
    fun refreshSearchResults_resetsSearchRequest_and_beginsSearch() =
        mainCoroutineRule.runBlockingTest {
            // Removing internet access should prevent the search from being performed
            // and thus leave `lastSearchResponse` and `searchRequest.pageNumber` at
            // their reset values.
            every { app.getString(any()) } returns ""
            every { Internet.hasConnection(any()) } returns false

            SearchViewModel(app, appRepository).apply {
                searchRequest = SearchRequest(categoryId = testId, pageNumber = 1234)
                lastSearchResponse = mockk<SearchResponse>(relaxed = true)

                refreshSearchResults()

                assertThat(lastSearchResponse).isNull()
                assertThat(searchRequest.pageNumber).isEqualTo(1)
            }
        }
}