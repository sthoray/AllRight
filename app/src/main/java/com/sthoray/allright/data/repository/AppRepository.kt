package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.model.search.SearchRequest

/**
 * Repository for handling all data operations.
 *
 * @property db the database to access
 */
class AppRepository(
    val db: AppDatabase
) {
    /** Get the category feature panel. */
    suspend fun getFeatureCategories() =
        RetrofitInstance.api.getFeatureCategories()

    /**
     * Search for listings.
     *
     * @param searchRequest the query to search for
     */
    suspend fun searchListings(searchRequest: SearchRequest) =
        RetrofitInstance.api.searchForListings(searchRequest)
}