package com.sthoray.allright.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.Category
import kotlinx.android.synthetic.main.item_filter_category.view.*

/** Adapter for adapting categories into a RecyclerView. */
class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    /** Responsible for displaying a single category. */
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    /** To calculate the differences between two lists. */
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * Inflate the view holder with a layout upon creation.
     *
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_filter_category, parent, false)
        return CategoryViewHolder(view)
    }

    /**
     * Bind the data found at [position] to the [holder].
     *
     * @param holder the ViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            tvCategory.text = category.name

            setOnClickListener {
                onItemClickListener?.let { it(category) }
            }
        }
    }

    /**
     * Return the number of items to display in this ViewHolder.
     *
     * @return the size of the current list
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Category) -> Unit)? = null

    /**
     * Set on click listener for a category.
     *
     * @param listener the on click listener lambda for a Listing
     */
    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }
}
