package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for a listing manager.
 *
 * Contains information about the seller of a particular listing.
 *
 * A manager might be an actual person such as a seller on the "second hand" marketplace
 * or a business store in the "mall" market place. This means the properties must be
 * accessed with care depending on other properties.
 *
 * Many fields are still missing from this data class and will likely need to be added
 * for the listing activity.
 *
 * @property id The manager's ID.
 * @property storeName The name of the store or manager (likely a mall listing).
 * @property firstName The seller's first name (likely a second hand listing).
 * @property storeWebsiteUrl The URL of the store's external website (likely a mall listing).
 * @property createdAt The time stamp that the store or user was created on.
 */
data class Manager(
    val id: Int,
    @SerializedName("name")
    val storeName: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("website")
    val storeWebsiteUrl: String?,
    @SerializedName("created_at")
    val createdAt: String?
)