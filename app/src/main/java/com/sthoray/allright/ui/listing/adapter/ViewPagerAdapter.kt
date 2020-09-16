package com.sthoray.allright.ui.listing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.model.listing.Listing
import kotlinx.android.synthetic.main.item_view_pager_listing_image.view.*

/** Adapter for images in the listing activity. */
class ViewPagerAdapter(
    private val images: List<Image>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    /** ViewHolder to represent the type for single item in the view pager. */
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Create a new [ViewPagerViewHolder] with a View to display an image in this adapter.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_pager_listing_image, parent, false)
        return ViewPagerViewHolder(view)
    }



    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return images.size
    }

    /**
     * Update the contents of the [ViewPagerViewHolder.itemView] to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val imageUrl = images[position].largeUrl
        holder.itemView.ivListingImage.load(imageUrl)


        //BENJAMIN
        holder.itemView.apply {

            setOnClickListener {
                onItemClickListener?.let { it(imageUrl) }
            }
        }

        //BENJAMIN



    }

    //BENJAMIN
    //
    private var onItemClickListener: ((String?) -> Unit)? = null

    fun setOnItemClickListener(imageUrl: (String?) -> Unit) {
        onItemClickListener = imageUrl
    }

    //
    //BENJAMIN

}