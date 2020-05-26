package com.sthoray.allright

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

const val baseUrl = "https://allgoods.co.nz/api/"

fun getTopLevel() {
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

fun searchCategory() {
    println("Attempting to post JSON")

    val url = baseUrl + "search"

    val searchObj = SearchRequest("3250") // search electronics category
    val jsonRequest = Gson().toJson(searchObj)

    val request = Request.Builder()
        .url(url)
        .build()

    val client = OkHttpClient()
}