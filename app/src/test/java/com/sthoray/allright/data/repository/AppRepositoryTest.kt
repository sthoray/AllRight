package com.sthoray.allright.data.repository

import com.google.common.truth.Truth.assertThat
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.search.SearchRequest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AppRepositoryTest {

    @MockK
    private lateinit var searchHistoryDatabase: SearchHistoryDatabase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    /**
     * TODO replace assertThat(response.code()).isEqualTo(200)
     *  with coVerify { RetrofitInstance.api.functionName() }
     *  for each function
     *  ...
     *  The Retrofit Instance test already tests the response code
     *
     */
    @Test
    fun getFeatureCategories() =
        runBlocking {
            val appRepository = AppRepository(searchHistoryDatabase)
            val response = appRepository.getFeatureCategories()
            assertThat(response.code()).isEqualTo(200)
        }

    @Test
    fun testGetSecondTierCategories() = runBlocking {
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = appRepository.getSecondTierCategories()
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun testSearchForListings() = runBlocking {
        val searchRequest = SearchRequest()
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = appRepository.searchListings(searchRequest)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun testGetListing() = runBlocking {
        val listingId = 1234
        val appRepository = AppRepository(searchHistoryDatabase)
        val response = appRepository.getListing(listingId)
        assertThat(response.code()).isEqualTo(200)
    }


}