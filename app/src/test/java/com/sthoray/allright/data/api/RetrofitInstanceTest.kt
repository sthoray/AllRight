package com.sthoray.allright.data.api


import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 *  RetrofitInstance unit test.
 *
 *
 * Since suspend functions need to be executed from a coroutine, we cannot just use it directly in the unit test,
 * that’s why we need to wrap it in runBlocking.
 * Note: runBlocking should only be used in the unit test but not in the production code because it will block the thread.
 */
class RetrofitInstanceTest {

    @Test
    fun testGetFeatureCategories() = runBlocking {
        // call the api
        val response = RetrofitInstance.api.getFeatureCategories()
        // verify the response is OK
        assertThat(response.code()).isEqualTo(200)

    }
}