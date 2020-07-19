package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for product manager.
 *
 * @property id the id.
 * @property managerName .
 * @property linkToWebsite .
 */
data class ListingManager (
    val id: Int,
    @SerializedName("name")
    val managerName: String,
    @SerializedName("website")
    val linkToWebsite: String

)