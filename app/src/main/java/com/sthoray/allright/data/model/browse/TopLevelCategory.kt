package com.sthoray.allright.data.model.browse

import com.google.gson.annotations.SerializedName

/**
 * Model representing a top level category.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property icon the name of the icon representing the category
 * @property listingCount the number of listings contained in the category
 */
data class TopLevelCategory(
    val id: Int,
    val name: String,
    val icon: String,
    @SerializedName("listing_count")
    val listingCount: Int
)