package com.sthoray.allright.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.Category
import com.sthoray.allright.utils.Constants.Companion.BASE_URL
import kotlinx.android.synthetic.main.item_layout_featured_category.view.*

/**
 * Adapter for adapting Featured Categories in Main Activity.
 */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.FeatureCategoryViewHolder>() {


    /** Responsible for displaying a single category. */
    inner class FeatureCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


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
     * @param parent the parent ViewGroup of this ViewHolder
     * @param viewType the id of the viewType
     *
     * @return a new [FeatureCategoryViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureCategoryViewHolder {
        return FeatureCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout_featured_category,
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
     * @param holder the DataViewHolder to bind to
     * @param position the index of the data to bind
     */
    override fun onBindViewHolder(holder: FeatureCategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            tvFeaturedCategoryName.text = category.name
            ivFeaturedCategory.load(BASE_URL + category.imagePath)

            setOnClickListener {
                onItemClickListener?.let { it(category) }
            }
        }
    }


    private var onItemClickListener: ((Category) -> Unit)? = null

    /**
     * Set on click listener for an item.
     *
     * @param listener the onclick listener lambda function
     */
    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }
}