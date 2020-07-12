package com.sthoray.allright.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model representing a featured category.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property icon the name of the icon representing the category
 * @property listingCount the number of listings contained in the category
 * @property imagePath the url path to an associated image
 * @property webPath the url path to browse the category in a web browser
 */
data class FeatureCategory(
    val name: String,
    val id: Int,
    val icon: String,
    @SerializedName("listing_count")
    val listingCount: Int,
    @SerializedName("image")
    val imagePath: String,
    @SerializedName("url")
    val webPath: String
)
