package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.user.Authentication

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

    /** Get the second tier categories. */
    suspend fun getSecondTierCategories() = RetrofitInstance.api.getSecondTierCategories()

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

    /**
     * Get the current user's profile.
     *
     * AllGoods uses bearer (or token) authentication. The `Authorization`
     * header token needs to be prefixed with 'Bearer' for this to work.
     *
     * @param bearerToken The bearerToken provided by a successful [login] request.
     */
    suspend fun getUserProfile(bearerToken: String) =
        RetrofitInstance.api.getUserProfile("Bearer $bearerToken")
}
