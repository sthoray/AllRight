package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for all the images of the product.
 *
 * Contains URLs for an items' main image and its thumbnail.
 *
 * @property id the id of the image.
 * @property largeUrl the fully qualified url for the main image
 * @property thumbUrl the fully qualified url for the thumbnail
 * @property number is image number.
 */
data class Image(
    val id: Int,
    @SerializedName("large_url")
    val largeUrl: String,
    @SerializedName("thumb_url")
    val thumbUrl: String,
    val number: Int
)