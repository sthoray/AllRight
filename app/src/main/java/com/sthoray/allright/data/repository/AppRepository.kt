package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.AppDatabase

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

    /** Search for listings. */
    suspend fun searchListings(searchQuery: String, categoryId: Int, pageNumber: Int) =
        RetrofitInstance.api.searchForListings(searchQuery, categoryId, pageNumber)
}