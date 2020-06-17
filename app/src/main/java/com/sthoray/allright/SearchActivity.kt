package com.sthoray.allright

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.search_result_row.view.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    /**
     * A query to search for.
     */
    private var searchQuery: SearchRequest = SearchRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_search)

        recyclerView_searchResults.layoutManager = LinearLayoutManager(this)

        // update nav bar title
        val navBarTitle = intent.getStringExtra(FeaturedCategoryViewHolder.CATEGORY_NAME_KEY)
        supportActionBar?.title = navBarTitle

        // perform search
        val categoryID = intent.getIntExtra(FeaturedCategoryViewHolder.CATEGORY_ID_KEY, 0)
        searchQuery.category_id = categoryID
        searchCategory(searchQuery)

        val btnMarketplaceSwitch: View = findViewById(R.id.button_switch)
        btnMarketplaceSwitch.setOnClickListener {
            searchQuery.toggleCategory()
            searchQuery.page = 1
            searchCategory(searchQuery)
        }
    }

    /**
     * Search a category.
     *
     * If the request was performed successfully, the recycler view is updated. If
     * the request fails for any reason, a message is printed to the console.
     */
    private fun searchCategory(searchObj: SearchRequest) {
        val baseUrl = "https://allgoods.co.nz/api/"
        val url = baseUrl + "search"

        val jsonBody = Gson().toJson(searchObj)

        val requestBody =
            jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())

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
                val items = searchResponse.data // array of SearchItem (max size = 24)

                runOnUiThread {
                    recyclerView_searchResults.adapter = SearchResultAdapter(items)
                }
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
    private class SearchResultAdapter(val searchItems: Array<SearchItem>) : RecyclerView.Adapter
    <SearchResultViewHolder>() {

        override fun getItemCount(): Int {
            return searchItems.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val searchResultView = layoutInflater.inflate(R.layout.search_result_row,
                parent,
                false)
            return SearchResultViewHolder(searchResultView)
        }

        override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
            val searchItem = searchItems[position]

            // Mall mappings
            holder.view.textView_productName.text = searchItem.name
            holder.view.textView_subtitle.text = searchItem.location_name // usually an item specific for mall
            holder.view.textView_priceLeft.text = "$" + searchItem.start_price.toString() // have to figure out what fields are best
            holder.view.textView_priceRight.text = searchItem.shipping.toString() // TODO: Map to free shipping or get cost from shipping_options etc.

            // Second hand mappings TODO: Map separate fields for second hand
            // holder.view.textView_productName.text = searchItem.name
            // holder.view.textView_subtitle.text = searchItem.location_name
            // holder.view.textView_priceLeft.text = searchItem.current_price.toString() // or start price
            // holder.view.textView_priceRight.text = searchItem.buy_now.toString()
            holder.view.imageView_productImage.load(searchItem.main_image.thumb_url)
            holder.searchItemId = searchItems[position].id
        }
    }

    /**
     * View holder for search results.
     *
     * Responsible for displaying a single search result and providing an on click listener.
     */
    private class SearchResultViewHolder(val view: View, var searchItemId : Int? = null) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val baseUrl = "https://www.allgoods.co.nz/product/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(baseUrl + searchItemId)
                println(searchItemId)
                view.context.startActivity(intent)
            }
        }
    }

    /**
     * Get the items on the next page (if any).
     */
    fun nextPage(@Suppress("UNUSED_PARAMETER")view: View) {
        // TODO: Check that we have not reached the last page
        searchQuery.page = searchQuery.page.inc()
        searchCategory(searchQuery)
    }

    /**
     * Get the items on the previous page (if any).
     */
    fun previousPage(@Suppress("UNUSED_PARAMETER")view: View) {
        if (searchQuery.page != 1) {
            searchQuery.page = searchQuery.page.dec()
            searchCategory(searchQuery)
        }
    }
}
