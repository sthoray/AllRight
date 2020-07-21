package com.sthoray.allright.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sthoray.allright.data.model.search.SearchRequest

/** Room database for the search history. */
@Database(
    entities = [SearchRequest::class],
    version = 1
)
@TypeConverters(SearchRequestConverters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {

    /**
     * Get a search history data access object.
     *
     * @return a [SearchHistoryDao]
     */
    abstract fun getSearchDao(): SearchHistoryDao

    companion object {

        @Volatile
        private var dbInstance: SearchHistoryDatabase? = null
        private val DB_LOCK = Any()

        /**
         * Create an instance of the listing database or return the existing one.
         *
         * This ensures that we only use one instance of the database throughout
         * the app.
         *
         * @return the existing [SearchHistoryDatabase], else a new one
         */
        operator fun invoke(context: Context) = dbInstance ?: synchronized(DB_LOCK) {
            // Create a database if it still does not exist once accessed
            dbInstance ?: createDatabase(context).also { dbInstance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SearchHistoryDatabase::class.java,
                "allright.db"
            ).build()
    }
}