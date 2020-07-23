package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for listing images.
 *
 * Contains URLs for an items' image and its thumbnail.
 *
 * @property id The id of the image.
 * @property largeUrl the fully qualified url to the full sized image.
 * @property thumbUrl the fully qualified url to the image's thumbnail.
 * @property number The position that this image should be displayed in the listing details activity.
 */
data class Image(
    val id: Int,
    @SerializedName("large_url")
    val largeUrl: String,
    @SerializedName("thumb_url")
    val thumbUrl: String,
    val number: Int
)