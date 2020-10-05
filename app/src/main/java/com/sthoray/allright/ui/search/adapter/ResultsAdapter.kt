package com.sthoray.allright.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.browse.CategoryFeature
import com.sthoray.allright.data.model.listing.Listing
import kotlinx.android.synthetic.main.item_layout_search.view.*
import kotlinx.android.synthetic.main.item_layout_search_feature_panel.view.*

/** Adapter for adapting listings into a RecyclerView. */
class ResultsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /** Responsible for displaying a single result. */
    inner class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /** Responsible for displaying a featured category panel. */
    inner class ResultsFeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<Listing>() {

        /**
         * Check if two listings are the same.
         *
         * @param oldItem the old [Listing]
         * @param newItem the new [Listing]
         *
         * @return true if they are the same, else false
         */
        override fun areItemsTheSame(oldItem: Listing, newItem: Listing): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Check if two listings have the same contents.
         *
         * @param oldItem the old [Listing]
         * @param newItem the new [Listing]
         *
         * @return true if they have the same contents, else false
         */
        override fun areContentsTheSame(oldItem: Listing, newItem: Listing): Boolean {
            return oldItem == newItem
        }
    }

    /** The list of search results. */
    val differSearchResults = AsyncListDiffer(this, differCallback)

    /** The view holder for the feature panel. */
    var featuredHolder: ResultsFeaturedViewHolder? = null

    /** The adapter for the feature panel. */
    var featuredCategoryAdapter: FeaturedCategoryAdapter? = null

    /** The list of featured categories. */
    var featuredPanel = listOf<CategoryFeature>()

    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            // Featured categories
            return ResultsFeaturedViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_layout_search_feature_panel,
                    parent,
                    false
                )
            )
        } else {
            // Search results
            return ResultsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_layout_search,
                    parent,
                    false
                )
            )
        }
    }

    /**
     * Return the number of items to display in this ViewHolder.
     *
     * @return the size of the current list
     */
    override fun getItemCount(): Int {
        return differSearchResults.currentList.size
    }

    /**
     * Bind the data found at [position] to the [holder].
     *
     * @param holder the ViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            // Featured categories
            // This block is not the most elegant way to proceed, but it works
            featuredHolder = holder as ResultsFeaturedViewHolder
            featuredCategoryAdapter = FeaturedCategoryAdapter()
            featuredCategoryAdapter?.differSearchFeaturedCategories?.submitList(featuredPanel)
            _featuredOnItemClickListener?.let { featuredCategoryAdapter?.setOnItemClickListener(it) }
            featuredHolder?.itemView?.apply {
                rvSearchFeaturePanel.apply {
                    adapter = featuredCategoryAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

        } else {
            // Search results
            val listing = differSearchResults.currentList[position - 1]
            holder.itemView.apply {
                // Mall mappings
                tvSearchName.text = listing.name
                tvSearchSubtitle.text = listing.locationName
                tvSearchPrice0.text = String.format(
                    context.getString(R.string.format_price),
                    listing.startPrice
                )
                tvSearchPrice1.text = ""
                ivSearchImage.load(listing.mainImage?.thumbUrl)

                setOnClickListener {
                    _onItemClickListener?.let { it(listing) }
                }
            }
        }
    }

    /**
     * Return the type of view at the position.
     *
     * This The first item in the recycler view should be featured categories (type 0).
     * All remaining items should be search results.
     *
     * @return 0 if the view type is featured categories, else 0 for search results.
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            1
        }
    }

    private var _onItemClickListener: ((Listing) -> Unit)? = null
    private var _featuredOnItemClickListener: ((CategoryFeature) -> Unit)? = null

    /**
     * Set the on click listener for a search result.
     *
     * @param listener The on click listener lambda for a Listing.
     */
    fun setOnItemClickListener(listener: (Listing) -> Unit) {
        _onItemClickListener = listener
    }

    /**
     * Set the on click listener for a featured panel item.
     *
     * @param listener The on click listener lambda for a featured category.
     */
    fun setFeaturedOnItemClickListener(listener: (CategoryFeature) -> Unit) {
        _featuredOnItemClickListener = listener
    }
}