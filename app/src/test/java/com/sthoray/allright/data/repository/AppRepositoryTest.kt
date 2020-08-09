package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Category
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AppRepositoryTest {

    @MockK
    private lateinit var searchHistoryDatabase: SearchHistoryDatabase

    @RelaxedMockK
    private lateinit var featureCategoriesResponse: FeatureCategoriesResponse

    @RelaxedMockK
    private lateinit var secondTierCategoriesResponse: List<Category>

    @RelaxedMockK
    private lateinit var searchForListingsResponse: SearchResponse

    @RelaxedMockK
    private lateinit var getListingResponse: Listing

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RetrofitInstance)
    }

    @Test
    fun getFeatureCategories() =
        runBlocking {
            val appRepository = AppRepository(searchHistoryDatabase)
            val response = Response.success(featureCategoriesResponse)

            coEvery { RetrofitInstance.api.getFeatureCategories() } returns response
            appRepository.getFeatureCategories()
            coVerify { RetrofitInstance.api.getFeatureCategories() }
        }

   /* @Test
    fun testGetSecondTierCategories() = runBlocking {
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = Response.success(secondTierCategoriesResponse)

        coEvery { RetrofitInstance.api.getSecondTierCategories() } returns response
        appRepository.getSecondTierCategories()
        coVerify { RetrofitInstance.api.getSecondTierCategories() }
    }

    @Test
    fun testSearchForListings() = runBlocking {
        val searchRequest = SearchRequest()
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = Response.success(searchForListingsResponse)


        coEvery { RetrofitInstance.api.searchForListings(searchRequest) } returns response
        appRepository.searchListings(searchRequest)
        coVerify { RetrofitInstance.api.searchForListings(searchRequest) }
    }

    @Test
    fun testGetListing() = runBlocking {
        val listingId = 1234
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = Response.success(getListingResponse)

        coEvery { RetrofitInstance.api.getListing(listingId) } returns response
        appRepository.getListing(listingId)
        coVerify { RetrofitInstance.api.getListing(listingId) }
    }*/

}