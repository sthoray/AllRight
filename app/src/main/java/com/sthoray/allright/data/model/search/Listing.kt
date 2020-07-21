package com.sthoray.allright.data.model.search

import com.google.gson.annotations.SerializedName

/**
 * Model for listings.
 *
 * This model (currently) does not contain all possible properties. The same
 * properties are not always used in both "Mall" and "Second hand"
 * marketplaces. This means some properties are nullable.
 *
 * @property id the listing id
 * @property productName the listing name
 * @property locationName the listing location
 * @property startPrice the listing start price
 * @property buyNowPrice the listing buy now price
 * @property currentPrice the current auction price
 * @property shippingType the type of shipping offered
 * @property shippingOptions a list of [ShippingOption]s
 * @property mainImage the listing [MainImage]
 */
data class Listing(
    val id: Int,
    @SerializedName("name")
    val productName: String,
    @SerializedName("location_name")
    val locationName: String,
    @SerializedName("start_price")
    val startPrice: Float?,
    @SerializedName("buy_now")
    val buyNowPrice: Float?,
    @SerializedName("current_price")
    val currentPrice: Float?,
    @SerializedName("shipping")
    val shippingType: Int,
    @SerializedName("shipping_options")
    val shippingOptions: List<ShippingOption>,
    @SerializedName("main_image")
    val mainImage: MainImage
)