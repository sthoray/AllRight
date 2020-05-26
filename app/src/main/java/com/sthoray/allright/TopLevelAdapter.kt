package com.sthoray.allright

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topcategory_row.view.*

/**
 * Adapter for topLevel categories.
 *
 * Populates the top level recycler view by creating top level view holders as required.
 *
 * @property topLevel the array containing [TopLevelCategory] to display
 * @constructor Creates an empty TopLevelAdapter
 */
class TopLevelAdapter(val topLevel: Array<TopLevelCategory>): RecyclerView.Adapter<TopLevelViewHolder>() {

    /**
     * Returns the number of categories to display in the recycler view (invoked by the layout manager).
     * @return the size of the [topLevel] data set
     */
    override fun getItemCount(): Int {
        return topLevel.count()
    }

    /**
     * Creates new views (invoked by the layout manager).
     * @param parent
     * @param viewType
     * @return a [TopLevelViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLevelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.topcategory_row, parent, false)
        return TopLevelViewHolder(cellForRow)
    }

    /**
     * Replace the contents of a view with data at the given [position] (invoked by the layout manager).
     * @param holder the ViewHolder to be updated
     * @param position the position of the item within this adapter's data set
     */
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
class TopLevelViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            println("CUSTOM VIEW HOLDER INIT TO STRING: " + toString())

            val intent = Intent(view.context, SearchResultsActivity::class.java)

            view.context.startActivity(intent)
        }
    }
}
