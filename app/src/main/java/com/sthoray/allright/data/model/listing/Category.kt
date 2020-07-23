package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model representing a category.
 *
 * @property id The category ID.
 * @property name The name of the category.
 * @property icon The name of the icon representing the category.
 * @property listingCount The number of listings contained in the category.
 * @property imagePath The url path to an associated image.
 * @property webPath The url path to browse the category in a web browser.
 */
data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    @SerializedName("listing_count")
    val listingCount: Int,
    @SerializedName("image")
    val imagePath: String?,
    @SerializedName("url")
    val webPath: String?
)