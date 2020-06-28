package com.sthoray.allright.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.FeatureCategory
import com.sthoray.allright.ui.search.view.SearchActivity
import kotlinx.android.synthetic.main.item_layout_main.view.*

/**
 * Adapter for populating the RecyclerView in Main Activity.
 *
 * @property featuredCategories the array containing [FeatureCategory]s
 */
class MainAdapter(private val featuredCategories: ArrayList<FeatureCategory>) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    /**
     * Responsible for displaying a single featured categories and providing an on
     * click listener.
     */
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Create OnClickListener for each itemView.
         *
         * When an itemView is tapped, [SearchActivity] will be launched to
         * begin searching the selected item's category.
         */
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, SearchActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }

        /**
         * Update all views within this view holder with the given category
         * information.
         *
         * @param category the category to display
         */
        fun bind(category: FeatureCategory) {
            itemView.apply {
                textViewCategoryName.text = category.name
                textViewListingCount.text = category.listingCount.toString()
                imageViewCategoryImage.load("https://allgoods.co.nz/" + category.imagePath)
            }
        }
    }

    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_layout_main, parent, false)
        )

    /**
     * Return the number of objects to display in this ViewHolder.
     *
     * @return the size of the [featuredCategories] list
     */
    override fun getItemCount(): Int = featuredCategories.size

    /**
     * Bind the data found at [position] to the [holder].
     *
     * @param holder the DataViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(featuredCategories[position])
    }

    /**
     * Add featured categories to this adapter clearing out any existing ones.
     *
     * @param featuredCategories the List of [FeatureCategory]s to add
     */
    fun addFeaturedCategories(featuredCategories: List<FeatureCategory>) {
        this.featuredCategories.apply {
            clear()
            addAll(featuredCategories)
        }
    }
}