package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for product categories.
 *
 * @property id the id.
 * @property categoryName the name of the category.
 * @property categoryIcon the icon for the category.
 */
data class ListingCategory (
    val id: Int,
    @SerializedName("name")
    val categoryName: String,
    @SerializedName("icon")
    val categoryIcon: String

)