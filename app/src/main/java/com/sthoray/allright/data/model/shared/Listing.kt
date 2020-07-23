package com.sthoray.allright.data.model.shared


import com.google.gson.annotations.SerializedName
import com.sthoray.allright.data.model.listing.*

/**
 * Model for listings.
 *
 * This model might still be missing a few properties.
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
 * @property related properties related to that specific listing [Related].
 * @property storeRelated properties related to the store where the listing came from [StoreRelated].
 *@property description details of the listing.
 * @property stats stats about the listing.
 * @property categories
 * @property brandTitle for the tile of the brand of the product.
 * @property outOfStock to check if the product is out of stock.
 * @property reserveMet to check if the reserve price has met.
 * @property reservePrice to check if the item has a reserve price.
 * @property manager
 * @property pickupLocation location of the product.
 * @property images list of all images for that listing [Image]
 */
data class Listing(
    val id: Int,
    @SerializedName("name")
    val productName: String,
    @SerializedName("location_name")
    val locationName: String,
    @SerializedName("start_price")
    val startPrice: Float,
    @SerializedName("buy_now")
    val buyNowPrice: Float?,
    @SerializedName("current_price")
    val currentPrice: Float?,
    @SerializedName("shipping")
    val shippingType: Int,
    @SerializedName("shipping_options")
    val shippingOptions: List<ShippingOption>,
    @SerializedName("main_image")
    val mainImage: MainImage,
    @SerializedName("related")
    val related: List<Related?>,
    @SerializedName("store_related")
    val storeRelated: List<StoreRelated?>,
    val description: String?,
    @SerializedName("stats")
    val stats: Stats?,
    @SerializedName("categories")
    val categories: List<Category?>,
    @SerializedName("brand_title")
    val brandTitle: String?,
    @SerializedName("out_of_stock")
    val outOfStock: Boolean?,
    @SerializedName("reserve_met")
    val reserveMet: Boolean?,
    @SerializedName("no_reserve")
    val reservePrice: Boolean?,
    @SerializedName("manager")
    val manager: Manager,
    @SerializedName("pickupLocation")
    val pickupLocation: pickpLocation?,
    @SerializedName("images")
    val images: List<Image?>


)