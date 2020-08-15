package com.sthoray.allright.data.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.sthoray.allright.TestUtil
import com.sthoray.allright.data.model.search.SearchRequest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class SearchHistoryDatabaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchHistoryDao: SearchHistoryDao
    private lateinit var db: SearchHistoryDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, SearchHistoryDatabase::class.java
        ).build()
        searchHistoryDao = db.getSearchDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSearchRequestAndReadInList() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.upsert(searchRequest)
        val history = searchHistoryDao.getHistory()

        assertThat(history.blockingObserve()?.get(0)).isEqualTo(searchRequest)
    }

    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}