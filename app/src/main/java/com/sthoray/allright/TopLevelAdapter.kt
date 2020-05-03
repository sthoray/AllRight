package com.sthoray.allright

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topcategory_row.view.*

/**
 * Adapter for topLevel categories.
 *
 * Provides data for the list view.
 */
class TopLevelAdapter(val topLevel: Array<TopLevelCategory>): RecyclerView.Adapter<TopLevelViewHolder>() {

    override fun getItemCount(): Int {
        return topLevel.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLevelViewHolder {
        // Create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.topcategory_row, parent, false)
        return TopLevelViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TopLevelViewHolder, position: Int) {
        val category = topLevel.get(position)

        holder.view.textView_name.text = category.name
        holder.view.textView_listingCount.text = category.listing_count.toString()
    }
}

class TopLevelViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}