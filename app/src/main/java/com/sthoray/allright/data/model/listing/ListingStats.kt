package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for product stats.
 *
 * @property id the id.
 * @property product_id the id of the product.
 * @property desktop_views to count desktop views of the product.
 * @property mobile_views to count mobile views of the product
 * @property tablet_views to count tablet views of the product
 */
data class ListingStats (
    val id: Int,
    val product_id: Int,
    val desktop_views: Int,
    val mobile_views: Int,
    val tablet_views: Int

)