package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for pick up location.
 *
 * @property id the id.
 * @property pickUpSuburb the suburb address.
 * @property pickUpCity city from where the product will be picked up.
 */
data class pickpLocation(
    val id: Int,
    @SerializedName("suburb")
    val pickUpSuburb: String,
    @SerializedName("city")
    val pickUpCity: String

)