package com.sthoray.allright.data.model.search

import com.google.gson.annotations.SerializedName

/**
 * Model for main images.
 *
 * Contains a listing's main and thumbnail image urls.
 *
 * @property largeUrl the fully qualified url for the main image
 * @property thumbUrl the fully qualified url for the thumbnail image
 */
data class MainImage(
    @SerializedName("large_url")
    val largeUrl: String,
    @SerializedName("thumb_url")
    val thumbUrl: String
)
