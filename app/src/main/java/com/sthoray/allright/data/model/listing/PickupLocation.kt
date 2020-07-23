package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for pick up location.
 *
 * @property suburb The suburb to pickup in.
 * @property city The city to pickup in
 */
data class PickupLocation   (
    @SerializedName("suburb")
    val suburb: String?,
    @SerializedName("city")
    val city: String?

)