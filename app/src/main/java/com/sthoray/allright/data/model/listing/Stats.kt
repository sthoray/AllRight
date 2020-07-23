package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for product stats.
 *
 * @property id the id.
 * @property productId the id of the product.
 * @property desktopViews to count desktop views of the product.
 * @property mobileViews to count mobile views of the product
 * @property tabletViews to count tablet views of the product
 */
data class Stats(
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("desktop_views")
    val desktopViews: Int,
    @SerializedName("mobile_views")
    val mobileViews: Int,
    @SerializedName("tablet_views")
    val tabletViews: Int

)