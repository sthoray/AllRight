package com.sthoray.allright.data.repository

import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchResponse
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test
import org.junit.experimental.categories.Category


class AppRepositoryTest {

    @MockK
    private lateinit var historyDatabase: SearchHistoryDatabase
    @RelaxedMockK
    private lateinit var featureCategoriesResponse: FeatureCategoriesResponse
    @RelaxedMockK
    private lateinit var secondTierCategoriesResponse : List<Category>
    @RelaxedMockK
    private lateinit var searchListingResponse: SearchResponse
    @RelaxedMockK
    private lateinit var getListingResponse: Listing
    @Test
    fun getFeatureCategories() {
    }

    @Test
    fun getSecondTierCategories() {
    }

    @Test
    fun searchListings() {
    }

    @Test
    fun getListing() {
    }
}