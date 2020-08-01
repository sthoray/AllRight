package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for listings.
 *
 * This model currently is missing many properties that may be required for the listing activity.
 *
 * @property id The listing id.
 * @property name The listing name.
 * @property locationName The listing location. Likely the string concatenation of [pickupLocation]s
 * properties if [pickupLocation] is not null. In this case, a property value of null is mapped
 * to "undefined".
 * @property startPrice The listing start price.
 * @property buyNowPrice The listing buy now price.
 * @property currentPrice The current auction price.
 * @property shippingType The type of shipping offered. The allowed values for the property are
 * currently unknown.
 * @property shippingOptions A list of [ShippingOption]s.
 * @property mainImage The main [Image] for this listing to be displayed in search results.
 * @property markdownOptions Formatting [MarkdownOptions] for the [description] field.
 * @property description The description of the listing. Some descriptions have markdown syntax, see
 * [markdownOptions].
 * @property categories The list of [Category]s that this listing is contained in.
 * @property outOfStock True if the product is out of stock, false otherwise.
 * @property manager The listing's [Manager].
 * @property pickups Determines if pickup are allowed. The allowed values for this property are
 * currently unknown.
 * @property pickupLocation The [PickupLocation] if pickups are allowed.
 * @property images The list of [Image]s for this listing.
 */
data class Listing(
    val id: Int,
    @SerializedName("name")
    val name: String,
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
    val mainImage: Image,
    @SerializedName("markdown_options")
    val markdownOptions: MarkdownOptions,
    val description: String?,
    val categories: List<Category>,
    @SerializedName("out_of_stock")
    val outOfStock: Boolean?,
    @SerializedName("manager")
    val manager: Manager,
    val pickups: Int,
    val pickupLocation: PickupLocation?,
    @SerializedName("images")
    val images: List<Image>
)