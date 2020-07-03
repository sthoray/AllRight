package com.sthoray.allright.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.SearchItem
import com.sthoray.allright.data.model.SearchMeta
import kotlinx.android.synthetic.main.item_layout_search.view.*

/**
 * Adapter for populating the RecyclerView in SearchActivity.
 *
 * @property searchItems
 */
class SearchAdapter(private val searchItems: ArrayList<SearchItem>) :
    RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>() {

    /**
     * Responsible for displaying a single item.
     */
    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Update all views within this view holder with the given item information.
         *
         * @param searchItem the item to display
         */
        fun bind(searchItem: SearchItem) {
            itemView.apply {
                // Mall mappings
                textViewProductName.text = searchItem.productName
                textViewSubtitle.text = searchItem.locationName
                textViewPrice0.text =
                    String.format(context.getString(R.string.format_price), searchItem.startPrice)
                textViewPrice1.text = searchItem.shipping.toString()
                imageViewProductImage.load(searchItem.mainImage.thumbUrl)
            }
        }
    }

    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        SearchItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout_search,
                parent,
                false
            )
        )

    /**
     * Return the number of objects to display in this ViewHolder.
     *
     * @return the size of the list to display
     */
    override fun getItemCount(): Int = searchItems.size

    /**
     * Bind the data found at [position] to the [holder].
     *
     * @param holder the DataViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(searchItems[position])
    }

    /**
     * Add search items to this adapter.
     *
     * If the search query changed, the page number will be reset to 1 and the
     * existing list should be cleared.
     *
     * @param searchItems a list of [SearchItem]s to add
     * @param searchMeta metadata defining the [searchItems]
     */
    fun addItems(searchItems: List<SearchItem>, searchMeta: SearchMeta) {
        this.searchItems.apply {
            if (searchMeta.pagination.currentPage == 1) {
                clear()
            }
            addAll(searchItems)
        }
    }
}