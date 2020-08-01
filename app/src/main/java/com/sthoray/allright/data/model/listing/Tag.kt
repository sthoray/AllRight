package com.sthoray.allright.data.model.listing

/**
 * Model for tags given to a listing.
 *
 * @property id This tag's numeric ID.
 * @property text The actual tag string.
 */
data class Tag(
    val id: Int?,
    val text: String?
)