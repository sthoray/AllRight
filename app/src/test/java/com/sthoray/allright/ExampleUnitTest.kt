package com.sthoray.allright

import okhttp3.OkHttpClient
import okhttp3.Request
import org.hamcrest.CoreMatchers.equalTo
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(Parameterized::class)
class ExampleUnitTest {

    private val browser: BrowseActivity = BrowseActivity()
    private val url  = "https://allgoods.co.nz/api/category/topLevel"
    private val url1 = "https://allgoods.co.nz/api/search"
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
        val responseBody = response.body?.string()
        val statusCode = response.code

        assertEquals(statusCode, 200)

        //Assert.assertTrue(!"403".equals(statusCode) && !"404".equals(statusCode) && !"400".equals(statusCode));
        //Assert.assertNotNull(statusCode);
    }
    /**
     * unit test for searching a category.
     *
     * It will return a status code of 200 on successfull call.
     */

    //@Test
    @Parameterized.Parameters
    fun getSerach_isCorrect(categoryId : Int)  {

        val client = OkHttpClient()
        val request =  Request.Builder().url(this.url1 + categoryId)
            .get()
            .build()

        val response = client.newCall(request)
            .execute()
        val responseBody = response.body?.string()
        val statusCode = response.code

        assertEquals(statusCode, 200)

        val jsonResponse = JSONObject(responseBody)
        val id = jsonResponse.getJSONObject("data")
            .getInt("id")
        assertThat(id, equalTo(categoryId))


    }
}
