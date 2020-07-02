package com.sthoray.allright.ui.search.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.data.model.SearchItem

/**
 * Adapter for populating the RecyclerView in SearchActivity.
 *
 * @property searchItems
 */
class SearchAdapter(private val searchItems: ArrayList<SearchItem>) :
    RecyclerView.Adapter<SearchAdapter.DataViewHolder>() {

    /**
     * Responsible for displaying a single item.
     */
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Update all views within this view holder with the given item information.
         *
         * @param item the item to display
         */
        fun bind(item: SearchItem) {
            itemView.apply {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}