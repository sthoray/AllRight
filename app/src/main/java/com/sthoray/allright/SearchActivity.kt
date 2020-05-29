package com.sthoray.allright

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_search)

        recyclerView_searchResults.layoutManager = LinearLayoutManager(this)

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

        // https://square.github.io/okhttp/upgrading_to_okhttp_4/
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
            val searchItem = searchItems.get(position)

            // Mall mappings
            holder.view.textView_productName.   text =            searchItem.name
            holder.view.textView_subtitle.      text =            searchItem.location_name // usually an item specific for mall
            holder.view.textView_priceLeft.     text =      "$" + searchItem.start_price .toString() // have to figure out what fields are best TODO: Format as price
            holder.view.textView_priceRight.    text =      "$" + searchItem.shipping    .toString() // TODO: map to free shipping or get cost from shipping_options etc.

            // Second hand mappings TODO: Figure out fields for second hand and map to UI
            // holder.view.textView_productName.text = searchItem.name
            // holder.view.textView_subtitle.text = searchItem.location_name
            // holder.view.textView_priceLeft.text = searchItem.current_price.toString() // or start price
            // holder.view.textView_priceRight.text = searchItem.buy_now.toString()
            holder.view.imageView_productImage.load(searchItem.main_image.thumb_url)
        }
    }

    /**
     * View holder for search results.
     *
     * Responsible for displaying a single search result and providing an on click listener.
     */
    private class SearchResultViewHolder(val view: View/*, val id : Int*/) : RecyclerView.ViewHolder(view) {
        // TODO: Add on click listener
        init {
            view.setOnClickListener {
                val url = "https://www.allgoods.co.nz/product/"//$id"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                view.context.startActivity(intent)
            }
        }
    }

}
