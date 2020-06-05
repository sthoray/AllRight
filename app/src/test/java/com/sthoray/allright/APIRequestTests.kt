package com.sthoray.allright

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * local unit test for API calls to the server, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class APIRequestTests {
    
    private val url = "https://allgoods.co.nz/api/category/topLevel"
    private val url1 = "https://allgoods.co.nz/api/search"
    private val url2 = "https://allgoods.co.nz/api/categoryFeaturePanel"

    /**
     * unit test for fetching the top level category.
     *
     * It will return a status code of 200 on successfull call.
     */
    @Test
    fun getRequest_isCorrect() {
        val client = OkHttpClient()
        val request = Request.Builder().url(url)
            .get()
            .build()

        val response = client.newCall(request)
            .execute()
        val statusCode = response.code

        assertEquals(statusCode, 200)
    }

    /**
     * unit test for Fetches featured categories.
     *
     * If the request was performed successfully, i will return status code 200.
     */
    @Test
    fun getFeatured_isCorrect() {
        val client = OkHttpClient()
        val request = Request.Builder().url(url2)
            .get()
            .build()

        val response = client.newCall(request)
            .execute()
        val statusCode = response.code

        assertEquals(statusCode, 200)
    }

    /**
     * unit test for searching a category.
     *
     * It will return a status code of 200 on successfull call.
     */

    @Test
    fun getSerach_isCorrect() {
        val categoryId = 1
        val jsonBody = Gson().toJson(SearchRequest(categoryId))
        val convertBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())
        val client = OkHttpClient()
        val request = Request.Builder().url(this.url1)
            .post(convertBody)
            .build()

        val response = client.newCall(request)
            .execute()
        val statusCode = response.code


        assertEquals(statusCode, 200)


    }
}
