package com.sthoray.allright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.featured_category_row.view.*
import okhttp3.*
import java.io.IOException

class BrowseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_topLevel.layoutManager = LinearLayoutManager(this)
        fetchTopLevel()
    }

    /**
     * Fetches top level categories.
     *
     * If the request was performed successfully, the recycler view is updated. If
     * the request fails for any reason, a message is printed to the console.
     */
    private fun fetchTopLevel() {
        val baseUrl = "https://allgoods.co.nz/api/"
        val url = baseUrl + "category/topLevel"
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val topLevels = Gson().fromJson<Array<TopLevelCategory>>(
                    responseBody,
                    Array<TopLevelCategory>::class.java)

                // UI must be updated from the main thread
                runOnUiThread {
                    recyclerView_topLevel.adapter = TopLevelAdapter(topLevels)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to request category/topLevel")
            }
        })
    }

    /**
     * Adapter for top level categories.
     *
     * Populates the top level recycler view by creating top level view holders as required.
     *
     * @property topLevel the array containing [TopLevelCategory]s
     * @constructor Creates a TopLevelAdapter with an array of categories
     */
    private class TopLevelAdapter(val topLevel: Array<TopLevelCategory>): RecyclerView.Adapter<TopLevelViewHolder>() {

        override fun getItemCount(): Int {
            return topLevel.count()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLevelViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val categoryRowView = layoutInflater.inflate(R.layout.top_level_row, parent, false)
            return TopLevelViewHolder(categoryRowView)
        }

        override fun onBindViewHolder(holder: TopLevelViewHolder, position: Int) {
            val category = topLevel.get(position)
            holder.view.textView_name.text = category.name
            holder.view.textView_listingCount.text = category.listing_count.toString()
        }
    }

    /**
     * View holder for top level categories.
     *
     * This is responsible for displaying a single top level category.
     */
    private class TopLevelViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }

}