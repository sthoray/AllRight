package com.sthoray.allright.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.search.Listing
import kotlinx.android.synthetic.main.item_layout_search.view.*

/**
 * Adapter for adapting listings into a RecyclerView.
 */
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ListingViewHolder>() {


    /** Responsible for displaying a single listing. */
    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    /** Callback for ListDiffer. */
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

    /** Calculate the differences between two lists. */
    val differ = AsyncListDiffer(this, differCallback)


    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        return ListingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout_search,
                parent,
                false
            )
        )
    }

    /**
     * Return the number of items to display in this ViewHolder.
     *
     * @return the size of the current list
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Bind the data found at [position] to the [holder].
     *
     * @param holder the ViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = differ.currentList[position]
        holder.itemView.apply {
            // Mall mappings
            textViewProductName.text = listing.productName
            textViewSubtitle.text = listing.locationName
            textViewPrice0.text = String.format(
                context.getString(R.string.format_price),
                listing.startPrice
            )
            textViewPrice1.text = listing.shippingType.toString()
            imageViewProductImage.load(listing.mainImage.thumbUrl)

            setOnClickListener {
                onItemClickListener?.let { it(listing) }
            }
        }
    }


    /** On click listener lambda function for a listing. */
    private var onItemClickListener: ((Listing) -> Unit)? = null

    /**
     * Set on click listener for a listing.
     *
     * @param listener the on click listener lambda for a Listing
     */
    fun setOnItemClickListener(listener: (Listing) -> Unit) {
        onItemClickListener = listener
    }
}