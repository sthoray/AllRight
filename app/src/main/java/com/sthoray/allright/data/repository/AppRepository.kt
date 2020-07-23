package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.shared.Listing
import com.sthoray.allright.data.model.search.SearchRequest

/**
 * Repository for handling all data operations.
 *
 * @property historyDatabase The database to access.
 */
class AppRepository(
    private val historyDatabase: SearchHistoryDatabase
) {
    /** Get the category feature panel. */
    suspend fun getFeatureCategories() =
        RetrofitInstance.api.getFeatureCategories()

    /** Get the top level categories. */
    suspend fun getTopLevelCategories() = RetrofitInstance.api.getTopLevelCategories()

    /**
     * Search for listings.
     *
     * @param searchRequest The [SearchRequest] query to search for.
     */
    suspend fun searchListings(searchRequest: SearchRequest) =
        RetrofitInstance.api.searchForListings(searchRequest)

    /**
     * Get all information about a listing.
     *
     * @param listingId The id of the [Listing] to fetch.
     */
    suspend fun getListing(listingId: Int) =
        RetrofitInstance.api.getListing(listingId)
}