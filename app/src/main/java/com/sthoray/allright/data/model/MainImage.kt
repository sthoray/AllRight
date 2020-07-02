package com.sthoray.allright.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model for a main image.
 *
 * Contains URLs for an items' main image and its thumbnail.
 *
 * @property largeUrl the fully qualified url for the main image
 * @property thumbUrl the fully qualified url for the thumbnail
 */
data class MainImage(
    @SerializedName("large_url")
    val largeUrl: String,
    @SerializedName("thumb_url")
    val thumbUrl: String
)