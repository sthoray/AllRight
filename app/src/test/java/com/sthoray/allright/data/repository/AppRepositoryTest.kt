package com.sthoray.allright.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Category
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response


class AppRepositoryTest {


    @MockK
    private lateinit var historyDatabase: SearchHistoryDatabase

    @RelaxedMockK
    private lateinit var featureCategoriesResponse: FeatureCategoriesResponse

    @RelaxedMockK
    private lateinit var secondTierCategoriesResponse: List<Category>

    @RelaxedMockK
    private lateinit var searchListingResponse: SearchResponse

    @RelaxedMockK
    private lateinit var getListingResponse: Listing

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RetrofitInstance)
    }

    @Test
    fun getFeatureCategories() = runBlocking {
        val appRepository = AppRepository(historyDatabase)
        val response = Response.success(featureCategoriesResponse)

        coEvery { RetrofitInstance.api.getFeatureCategories() } returns response
        appRepository.getFeatureCategories()
        coVerify { RetrofitInstance.api.getFeatureCategories() }

    }

    @Test
    fun getSecondTierCategories() = runBlocking {
        val appRepository = AppRepository(historyDatabase)
        val response = Response.success(secondTierCategoriesResponse)

        coEvery { RetrofitInstance.api.getSecondTierCategories() } returns response
        appRepository.getSecondTierCategories()
        coVerify { RetrofitInstance.api.getSecondTierCategories() }
    }


    @Test
    fun searchListings() {
    }

    @Test
    fun getListing() {
    }
}