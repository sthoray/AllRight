package com.sthoray.allright

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topcategory_row.view.*

/**
 * Adapter for topLevel categories.
 */
class TopLevelAdapter: RecyclerView.Adapter<TopLevelViewHolder>() {

    // temp lists for testing recycler view
    val categoryNames = listOf<String>("First","Second","Third")
    val categoryItemCount = listOf<Int>(55, 125, 0)

    override fun getItemCount(): Int {
        return categoryNames.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLevelViewHolder {
        // Create a view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.topcategory_row, parent, false)
        return TopLevelViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TopLevelViewHolder, position: Int) {
        val categoryName = categoryNames.get(position)
        val categoryItemCount = categoryItemCount.get(position)

        holder.view.textView_name.text = categoryName
        holder.view.textView_itemCount.text = categoryItemCount.toString()
    }
}

class TopLevelViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}