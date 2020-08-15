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
import com.sthoray.allright.utils.SortOrder
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
    fun writeSearchRequest_readInHistory() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.upsert(searchRequest)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.get(0)).isEqualTo(searchRequest)
    }

    @Test
    @Throws(Exception::class)
    fun writeMultipleSearchRequests_readInHistory() = runBlocking<Unit> {
        val searchRequest1: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 0
        }
        val searchRequest2: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 1
        }
        val searchRequest3: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 2
        }
        searchHistoryDao.upsert(searchRequest1)
        searchHistoryDao.upsert(searchRequest2)
        searchHistoryDao.upsert(searchRequest3)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(3)
        assertThat(history?.get(0)).isEqualTo(searchRequest1)
        assertThat(history?.get(1)).isEqualTo(searchRequest2)
        assertThat(history?.get(2)).isEqualTo(searchRequest3)
    }

    @Test
    @Throws(Exception::class)
    fun writeDuplicateSearchRequest_readNoDuplicatesInHistory() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.upsert(searchRequest)
        searchHistoryDao.upsert(searchRequest)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(1)
        assertThat(history?.get(0)).isEqualTo(searchRequest)
    }

    @Test
    @Throws(Exception::class)
    fun updateSearchRequest_readUpdatedInHistory() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.upsert(searchRequest)
        searchRequest.sortBy = SortOrder.ALPHABETICAL.key
        searchHistoryDao.upsert(searchRequest)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(1)
        assertThat(history?.get(0)).isEqualTo(searchRequest)
    }

    @Test
    @Throws(Exception::class)
    fun writeThenDeleteSearchRequest_readNothingInHistory() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.upsert(searchRequest)
        searchHistoryDao.delete(searchRequest)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(0)
    }

    @Test
    @Throws(Exception::class)
    fun writeMultipleSearchRequestsThenDeleteOne_readRemainderInHistory() = runBlocking<Unit> {
        val searchRequest1: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 0
        }
        val searchRequest2: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 1
        }
        val searchRequest3: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 2
        }
        searchHistoryDao.upsert(searchRequest1)
        searchHistoryDao.upsert(searchRequest2)
        searchHistoryDao.upsert(searchRequest3)
        searchHistoryDao.delete(searchRequest2)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(2)
        assertThat(history?.get(0)).isEqualTo(searchRequest1)
        assertThat(history?.get(1)).isEqualTo(searchRequest3)
    }

    @Test
    @Throws(Exception::class)
    fun deleteSearchRequestFromEmptyTable_readNothingInHistory() = runBlocking<Unit> {
        val searchRequest: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 3
        }
        searchHistoryDao.delete(searchRequest)
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(0)
    }

    @Test
    @Throws(Exception::class)
    fun writeMultipleSearchRequestsThenClearHistory_readNothingInHistory() = runBlocking<Unit> {
        val searchRequest1: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 0
        }
        val searchRequest2: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 1
        }
        val searchRequest3: SearchRequest = TestUtil.createSearchRequest().apply {
            categoryId = 2
        }
        searchHistoryDao.upsert(searchRequest1)
        searchHistoryDao.upsert(searchRequest2)
        searchHistoryDao.upsert(searchRequest3)
        searchHistoryDao.clearHistory()
        val history = searchHistoryDao.getHistory().blockingObserve()

        assertThat(history?.size).isEqualTo(0)
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
