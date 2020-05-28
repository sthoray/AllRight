package com.sthoray.allright

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

const val baseUrl = "https://allgoods.co.nz/api/"

/**
 * Perform HTTP GET request for top level categories.
 */
fun getTopLevelExample() {
    println("Attempting to fetch JSON")

    val url = baseUrl + "category/topLevel"
    val request = Request.Builder()
        .url(url)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object: Callback { // execute the request on a background thread
        override fun onResponse(call: Call, response: Response) {
            println("Received response")
            val body = response.body?.string()

            // category/topLevel returns an array of objects
            val gson = Gson()
            val topLevel: Array<TopLevelCategory> =
                gson.fromJson(body, Array<TopLevelCategory>::class.java)

            // do stuff with the topLevel array
            println(topLevel)
        }

        override fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request")
        }
    })
}

/**
 * Perform HTTP POST request for search.
 */
fun searchCategoryExample() {
    println("Attempting to post JSON")

    val url = baseUrl + "search"

    val searchObj = SearchRequest(3250) // search electronics category
    val jsonBody = Gson().toJson(searchObj)

    // we should not used the deprecated create method! This should be updated:
    // https://square.github.io/okhttp/upgrading_to_okhttp_4/
    val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), jsonBody)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object: Callback {
        override fun onResponse(call: Call, response: Response) {
            println("Received response")
            val body = response.body?.string() // the search response could be large, be careful

            val gson = Gson()
            val searchResponse = gson.fromJson(body,SearchResponse::class.java)
            val items = searchResponse.data // list of items (max size = 24)

            println(items)
        }

        override fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request")
        }
    })
}
