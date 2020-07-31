package com.sthoray.allright.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.Category

import kotlinx.android.synthetic.main.item_layout_top_level_category.view.*

/** Adapter for adapting top level categories into a RecyclerView. */
class BrowseAdapter : RecyclerView.Adapter<BrowseAdapter.TopLevelCategoryViewHolder>() {

    /** Responsible for displaying a single top level category. */
    inner class TopLevelCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Category,
            newItem: Category
        ): Boolean {
            return oldItem == newItem
        }
    }

    /** Calculate the difference between two lists. */
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLevelCategoryViewHolder {
        return TopLevelCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout_top_level_category,
                parent,
                false
            )
        )
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Update the contents of the ViewHolder's itemView to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: TopLevelCategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            textViewCategoryName.text = category.name
            // TODO Use actual icons instead of the string
            textViewIcon.text = category.icon

            setOnClickListener {
                onItemClickListener?.let { it(category) }
            }
        }
    }

    private var onItemClickListener: ((Category) -> Unit)? = null

    /**
    * Set the on click listener for an itemView.
    *
    * @param listener the onclick listener lambda function
    */
    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }
}