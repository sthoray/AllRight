package com.sthoray.allright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_search)

        recyclerView_searchResults.layoutManager = LinearLayoutManager(this)
        recyclerView_searchResults.adapter = SearchResultAdapter()

        // update nav bar title
        val navBarTitle = intent.getStringExtra(FeaturedCategoryViewHolder.CATEGORY_NAME_KEY)
        supportActionBar?.title = navBarTitle

        // perform search TODO: Double check default value i.e. value when searching all categories
        val categoryID = intent.getIntExtra(FeaturedCategoryViewHolder.CATEGORY_ID_KEY, 0)
        searchCategory(categoryID)
    }

    /**
     * Search a category.
     *
     * If the request was performed successfully, the recycler view is updated. If
     * the request fails for any reason, a message is printed to the console.
     */
    private fun searchCategory(categoryID: Int) {
        val baseUrl = "https://allgoods.co.nz/api/"
        val url = baseUrl + "search"

        val searchObj = SearchRequest(categoryID)
        val jsonBody = Gson().toJson(searchObj)

        // TODO: remove deprecated create method
        // https://square.github.io/okhttp/upgrading_to_okhttp_4/
        val requestBody =
            RequestBody.create("application/json; charset=utf-8".toMediaType(), jsonBody)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Received response")
                val body = response.body?.string()

                val gson = Gson()
                val searchResponse = gson.fromJson(body, SearchResponse::class.java)
                val items = searchResponse.data // list of items (max size = 24)

                println(items.get(0).name)
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute search request")
            }
        })
    }

    /**
     * Adapter for search results.
     *
     * Populates the search results recycler view by creating search result view holders
     * as required.
     */
    private class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

        override fun getItemCount(): Int {
            return 20
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val searchResultView = layoutInflater.inflate(R.layout.search_result_row,
                parent,
                false)
            return SearchResultViewHolder(searchResultView)
        }

        override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {

        }

    }

    /**
     * View holder for search results.
     *
     * Responsible for displaying a single search result and providing an  on click listener.
     */
    private class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

}
