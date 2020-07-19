package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for product stats.
 *
 * @property id the id.
 * @property product_Id the id of the product.
 * @property desktop_Views to count desktop views of the product.
 * @property mobile_Views to count mobile views of the product
 * @property tablet_Views to count tablet views of the product
 */
data class ListingStats(
    val id: Int,
    @SerializedName("product_id")
    val product_Id: Int,
    @SerializedName("desktop_views")
    val desktop_Views: Int,
    @SerializedName("mobile_views")
    val mobile_Views: Int,
    @SerializedName("tablet_views")
    val tablet_Views: Int

)