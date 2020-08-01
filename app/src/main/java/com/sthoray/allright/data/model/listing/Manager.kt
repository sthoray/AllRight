package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for a listing manager.
 *
 * Contains information about the seller of a particular listing.
 *
 * A manager might be an actual person such as a seller on the "secondhand" marketplace
 * or a business store in the "mall" market place. This means the properties must be
 * accessed with care depending on the marketplace.
 *
 * Many fields are still missing from this data class and will likely need to be added.
 * The "active_offer" field could be particularly useful for the listing activity.
 *
 * @property id The manager's ID.
 * @property storeName The name of the store or manager for a mall listing.
 * @property logo The stores logo image containing a [Image.thumbUrl] and [Image.largeUrl]. Used in mall listings.
 * @property firstName The seller's first name for a secondhand listing.
 * @property avatar The seller's avatar image containing a [Image.thumb] and [Image.large]. Used in secondhand listings.
 * @property storeWebsiteUrl The URL of the store's external website. Used in mall listings.
 * @property createdAt The time stamp that the store or user was created on.
 * @property locationName The location of the store for a mall listing.
 */
data class Manager(
    val id: Int?,
    @SerializedName("name")
    val storeName: String?,
    val logo: Image?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("image")
    val avatar: Image?,
    @SerializedName("website")
    val storeWebsiteUrl: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("location_name")
    val locationName: String?
)