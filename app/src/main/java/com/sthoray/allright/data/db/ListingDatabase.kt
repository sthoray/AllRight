package com.sthoray.allright.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sthoray.allright.data.model.search.Listing

/**
 * Room database class for listings.
 */
@Database(
    entities = [Listing::class],    // List of tables
    version = 1                     // Update if the database is changed
)
abstract class ListingDatabase : RoomDatabase() {

    /**
     * Get a listing DAO.
     *
     * @return a [ListingDao]
     */
    abstract fun getListingDao(): ListingDao

    companion object {

        /** Singleton instance of the listing database. */
        private var dbInstance: ListingDatabase? = null

        /** Used to synchronise setting [dbInstance] from multiple threads. */
        private val DB_LOCK = Any()

        /**
         * Create an instance of the listing database or return the existing one.
         *
         * This ensures that we only use one instance of the database throughout
         * the app.
         *
         * @return the existing [ListingDatabase], else a new one
         */
        operator fun invoke(context: Context) = dbInstance ?: synchronized(DB_LOCK) {
            // Create a database if it still does not exist once accessed
            dbInstance ?: createDatabase(context).also { dbInstance = it }
        }

        /**
         * Create a new Room database for listings.
         *
         * @return a new Room [ListingDatabase]
         */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ListingDatabase::class.java,
                "listing.db"
            ).build()
    }
}
