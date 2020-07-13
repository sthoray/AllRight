package com.sthoray.allright.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sthoray.allright.data.model.search.Listing

/**
 * Defines interactions for [Listing]s in a Room database.
 */
@Dao
interface ListingDao {

    /**
     * Update and insert a listing into the database.
     *
     * @param listing the [Listing] to insert
     *
     * @return the id of the inserted listing
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(listing: Listing): Long

    /**
     * Get all listings in the database.
     *
     * @return a list of [Listing]s contained in the database
     */
    @Query("SELECT * FROM listings")
    fun getAllListings(): LiveData<List<Listing>>

    /**
     * Delete a listing form the database.
     *
     * @param listing the [Listing] to delete
     */
    @Delete
    suspend fun deleteListing(listing: Listing)
}