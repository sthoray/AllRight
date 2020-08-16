package com.sthoray.allright.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sthoray.allright.data.model.search.SearchRequest

/** Defines interactions with the search history in a Room database. */
@Dao
interface SearchHistoryDao {

    /**
     * Update and insert a [SearchRequest] into the database.
     *
     * The uniqueness of a search request is defined in [SearchRequest] data class.
     *
     * @param searchRequest the [SearchRequest] to save
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(searchRequest: SearchRequest)

    /**
     * Get all past searches.
     *
     * @return a list of [SearchRequest]s contained in the database
     */
    @Query("SELECT * FROM search_history")
    fun getHistory(): LiveData<List<SearchRequest>>

    /**
     * Delete a search form the search history.
     *
     * @param searchRequest the [searchRequest] to delete
     */
    @Delete
    suspend fun delete(searchRequest: SearchRequest)

    /** Clears the entire search history. */
    @Query("DELETE FROM search_history")
    suspend fun clearHistory()
}
