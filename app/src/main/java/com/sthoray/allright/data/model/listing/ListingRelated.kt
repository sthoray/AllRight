package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for the anything which is related to an individual product.
 *
 * @property id the id.
 * @property store_id the id of the store.
 * @property longitude the longitude of the product.
 * @property latitude the latitude of the product.
 */
data class ListingRelated(
    val id: Int,
    val store_Id: Int,
    @SerializedName("lng")
    val longitude: String,
    @SerializedName("lat")
    val latitude: String

)