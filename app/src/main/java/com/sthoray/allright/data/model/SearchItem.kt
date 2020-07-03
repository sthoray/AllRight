package com.sthoray.allright.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model for search item.
 *
 * This model (currently) does not contain all possible properties. The same
 * properties are not always used in both "Mall" and "Second hand"
 * marketplaces. This means some properties are nullable.
 *
 * @property id the id of the listing
 * @property productName the name of the listing
 * @property locationName the location name of the listing
 * @property startPrice the starting price of the listing
 * @property buyNow the buy now price of the listing
 * @property currentPrice the current auction price of the listing
 * @property shipping the type of shipping offered
 * @property shippingOptions a list of [ShippingOption]s
 * @property mainImage the main image of the listing
 */
data class SearchItem(
    val id: Int,
    @SerializedName("name")
    val productName: String,
    @SerializedName("location_name")
    val locationName: String,
    @SerializedName("start_price")
    val startPrice: Float?,
    @SerializedName("buy_now")
    val buyNow: Float?,
    @SerializedName("current_price")
    val currentPrice: Float?,
    val shipping: Int,
    @SerializedName("shipping_options")
    val shippingOptions: List<ShippingOption>,
    @SerializedName("main_image")
    val mainImage: MainImage
)
