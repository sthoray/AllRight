package com.sthoray.allright.data.api


import com.google.common.truth.Truth
import com.sthoray.allright.util.MockResponseFileReader
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

/**
 *  RetrofitInstance unit test.
 *
 * Since suspend functions need to be executed from a coroutine, we cannot just use it directly in the unit test,
 * that’s why we need to wrap it in runBlocking.
 * Note: runBlocking should only be used in the unit test but not in the production code because it will block the thread.
 */
class RetrofitInstanceTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        RetrofitInstance.baseUrl = mockWebServer.url("/")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        // Each time we start a new MockWebserver, the url changes. To allow the new baseUrl
        // from setUp() to take effect, we must reset the retrofit instance first.
        RetrofitInstance.resetInstance()
    }

    @Test
    fun readFeatureCategoriesSuccessJsonFile() {
        val reader = MockResponseFileReader("feature_categories_success_response.json")
        Truth.assertThat(reader).isNotNull()
    }

    @Test
    fun readSecondTierCategoriesSuccessJsonFile() {
        val reader = MockResponseFileReader("second_tier_categories_success_response.json")
        Truth.assertThat(reader).isNotNull()
    }

    @Test
    fun readSearchSuccessJsonFile() {
        val reader = MockResponseFileReader("search_category_3250_success_response.json")
        Truth.assertThat(reader).isNotNull()
    }

    @Test
    fun readBrowseCategorySuccessJsonFile() {
        val reader = MockResponseFileReader("browse_category_3250_success_response.json")
        Truth.assertThat(reader).isNotNull()
    }

    @Test
    fun readListingSuccessJsonFile() {
        val reader = MockResponseFileReader("listing_3937958_success_response.json")
        Truth.assertThat(reader).isNotNull()
    }

    @Test
    fun testGetFeatureCategories_returnsResponseCode200() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("feature_categories_success_response.json").content)
        mockWebServer.enqueue(response)

        val actualResponse = RetrofitInstance.api.getFeatureCategories()

        Truth.assertThat(response.toString().contains("200")).isEqualTo(
            actualResponse.code().toString().contains("200")
        )
    }

    @Test
    fun testGetSecondTierCategories_returnsResponseCode200() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("second_tier_categories_success_response.json").content)
        mockWebServer.enqueue(response)

        val actualResponse = RetrofitInstance.api.getSecondTierCategories()

        Truth.assertThat(response.toString().contains("200")).isEqualTo(
            actualResponse.code().toString().contains("200")
        )
    }

    @Test
    fun testSearchForListings_returnsResponseCode200() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("search_category_3250_success_response.json").content)
        mockWebServer.enqueue(response)

        val actualResponse = RetrofitInstance.api.searchForListings()

        Truth.assertThat(response.toString().contains("200")).isEqualTo(
            actualResponse.code().toString().contains("200")
        )
    }

    @Test
    fun testBrowseCategory_returnsResponseCode200() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("browse_category_3250_success_response.json").content)
        mockWebServer.enqueue(response)

        val actualResponse = RetrofitInstance.api.browseCategory(32500, 1)

        Truth.assertThat(response.toString().contains("200")).isEqualTo(
            actualResponse.code().toString().contains("200")
        )
    }


    @Test
    fun testGetListing_returnsResponseCode200() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("listing_3937958_success_response.json").content)
        mockWebServer.enqueue(response)

        val actualResponse = RetrofitInstance.api.getListing(3937958)

        Truth.assertThat(response.toString().contains("200")).isEqualTo(
            actualResponse.code().toString().contains("200")
        )
    }
}