package com.sthoray.allright.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sthoray.allright.data.model.search.Listing

/**
 * Room database class for this app.
 */
@Database(
    entities = [Listing::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Get a listing DAO.
     *
     * @return a [ListingDao]
     */
    abstract fun getListingDao(): ListingDao

    companion object {

        @Volatile
        private var dbInstance: AppDatabase? = null
        private val DB_LOCK = Any()

        /**
         * Create an instance of the listing database or return the existing one.
         *
         * This ensures that we only use one instance of the database throughout
         * the app.
         *
         * @return the existing [AppDatabase], else a new one
         */
        operator fun invoke(context: Context) = dbInstance ?: synchronized(DB_LOCK) {
            // Create a database if it still does not exist once accessed
            dbInstance ?: createDatabase(context).also { dbInstance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "allright.db"
            ).build()
    }
}