package com.sthoray.allright

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_explore.*
import kotlinx.android.synthetic.main.featured_category_row.view.*
import okhttp3.*
import java.io.IOException

class ExploreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        recyclerView_featuredCategories.layoutManager = GridLayoutManager(this, 3)
        getFeatured()
    }

    /**
     * Fetches featured categories.
     *
     * If the request was performed successfully, the recycler view is updated. If
     * the request fails for any reason, a message is printed to the console.
     */
    fun getFeatured() {
        val baseUrl = "https://allgoods.co.nz/api/"
        val url = baseUrl + "categoryFeaturePanel"
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()

        // execute the request on a background thread
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val featuredPanel = Gson().fromJson<FeaturePanelCategory>(
                    responseBody,
                    FeaturePanelCategory::class.java)

                // convert the response into a list
                val featuredCategories = ArrayList(featuredPanel.categories.values)

                // UI must be updated from the main thread
                runOnUiThread {
                    recyclerView_featuredCategories.adapter = FeaturedCategoriesAdapter(featuredCategories)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to request categoryFeaturePanel")
            }
        })
    }

    /**
     * Adapter for featured categories.
     *
     * Populates the featured panel recycler view by creating featured view holders as required.
     *
     * @property featuredCategories the array containing [FeaturePanelCategory]s
     * @constructor Creates a FeaturedCategoriesAdapter with an array of categories
     */
    private class FeaturedCategoriesAdapter(val featuredCategories: ArrayList<FeatureCategory>)
        : RecyclerView.Adapter<FeaturedCategoryViewHolder>() {

        override fun getItemCount(): Int {
            return featuredCategories.count()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : FeaturedCategoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val categoryItemView = layoutInflater.inflate(R.layout.featured_category_row,
                parent,
                false)
            return FeaturedCategoryViewHolder(categoryItemView)
        }

        override fun onBindViewHolder(holder: FeaturedCategoryViewHolder, position: Int) {
            val category = featuredCategories.get(position)
            holder.view.textView_name.text = category.name
            holder.view.textView_listingCount.text = category.listing_count.toString()

            // TODO: add featured category images here -- Look at [FeatureCategory] for fields
        }
    }

    /**
     * View holder for featured categories.
     *
     * Responsible for displaying a single featured categories and providing an on
     * click listener.
     */
    private class FeaturedCategoryViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                // start the search activity
                val intent = Intent(view.context, SearchActivity::class.java)
                view.context.startActivity(intent)
            }
        }

    }

}
