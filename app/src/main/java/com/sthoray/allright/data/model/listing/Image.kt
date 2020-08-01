package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for listing images.
 *
 * Contains URLs for an items' image and its thumbnail.
 *
 * @property id The ID of the image.
 * @property largeUrl The fully qualified url to the full sized image.
 * @property large The fully qualified url to the full sized image. Only used for a users avatar.
 * @property thumbUrl The fully qualified url to the image thumbnail.
 * @property thumb The fully qualified url to the image thumbnail. Only used for a users avatar.
 * @property number The position that this image should be displayed in the listing details activity.
 */
data class Image(
    val id: Int?,
    @SerializedName("large_url")
    val largeUrl: String?,
    val large: String?,
    @SerializedName("thumb_url")
    val thumbUrl: String?,
    val thumb: String?,
    val number: Int?
)