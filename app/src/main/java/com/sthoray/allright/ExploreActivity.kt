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
import coil.api.load
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
        //Increment the espresso idling resource for testing purposes
        EspressoIdlingResource.increment()

        // execute the request on a background thread
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                //Increment the espresso idling resource for testing purposes
                EspressoIdlingResource.increment()

                val responseBody = response.body?.string()
                val featuredPanel = Gson().fromJson<FeaturePanelCategory>(
                    responseBody,
                    FeaturePanelCategory::class.java)

                // convert the response into a list
                val featuredCategories = ArrayList(featuredPanel.categories.values)

                // UI must be updated from the main thread
                runOnUiThread {
                    recyclerView_featuredCategories.adapter = FeaturedCategoriesAdapter(featuredCategories)

                    //Decrement the espresso idling resource for testing purposes
                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to request categoryFeaturePanel")
            }

        })
        //Decrement the espresso idling resource for testing purposes
        EspressoIdlingResource.decrement()
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
            holder.view.imageView_image.load("https://allgoods.co.nz/" + category.image)
            holder.category = category
        }
    }
}

/**
 * View holder for featured categories.
 *
 * Responsible for displaying a single featured categories and providing an on
 * click listener.
 */
class FeaturedCategoryViewHolder(val view: View, var category: FeatureCategory? = null): RecyclerView.ViewHolder(view) {

    companion object {
        const val CATEGORY_NAME_KEY = "CATEGORY_NAME"
        const val CATEGORY_ID_KEY = "CATEGORY_ID"
    }

    init {
        view.setOnClickListener {
            // start the search activity
            val intent = Intent(view.context, SearchActivity::class.java)

            intent.putExtra(CATEGORY_NAME_KEY, category?.name)
            intent.putExtra(CATEGORY_ID_KEY, category?.id)

            view.context.startActivity(intent)
        }
    }
}