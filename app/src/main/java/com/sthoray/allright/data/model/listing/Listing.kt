package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for listings.
 *
 * This model currently is missing properties that may be required for special marketplaces such as vehicles.
 *
 * @property id The listing ID.
 * @property listingType The type of listing. The values for this property needs investigating.
 * @property name The name of the listing.
 * @property description The listing's description. Some descriptions use markdown syntax, see [markdownOptions].
 * @property markdownOptions Formatting [MarkdownOptions] for the [description] field.
 * @property categories The list of [Category]s that this listing is contained in.
 * @property manager The listing's [Manager].
 * @property pickups 1 if they buyer can pickup, 2 if the buyer may not pickup, or 3 if the buyer must pickup.
 * @property locationName The name of the pickup location if pickups are allowed. This essentially concatenates the properties stored in [pickupLocation].
 * @property pickupLocation The [PickupLocation] if pickups are allowed.
 * @property mainImage The main [Image] for this listing to be displayed in search results.
 * @property images The list of [Image]s for this listing. The [mainImage] is included in this list.
 * @property properties A list of [Property]s to be displayed under the "item specifics" header.
 * @property tags The listing's list of [Tag]s.
 * @property shipping The type of shipping offered. The values of this property needs investigating to determine it significance.
 * @property shippingOptions The list of [ShippingOption]s available for the listing.
 * @property combinedShipping 1 if shipping can be combined with other listings from the same seller, 0 otherwise.
 * @property startPrice The starting price of the listing in the secondhand marketplace. In the Mall, this is the "buy now" purchase price.
 * @property displayPrice The pre-discounted price of a product in the mall marketplace. Often null or the same as [startPrice] when the listing is not discounted.
 * @property brandNew 1 if the listing is a brand new item, 0 otherwise.
 * @property outOfStock True if the product is out of stock, false otherwise.
 * @property buyNowPrice The secondhand listing's buy now price.
 * @property currentPrice The secondhand listing's current auction price.
 * @property reserveMet True if [currentPrice] is greater than the reserve price, false otherwise.
 * @property noReserve True if there is a reserve price, false otherwise.
 * @property expires The time stamp when the secondhand listing will close.
 * @property paymentOptions A list of [PaymentOption]s for secondhand listings.

 */
data class Listing(
    val id: Int?,
    @SerializedName("listing_type")
    val listingType: Int?,
    val name: String?,
    val description: String?,
    @SerializedName("markdown_options")
    val markdownOptions: MarkdownOptions?,
    val categories: List<Category>?,
    @SerializedName("manager")
    val manager: Manager?,
    val pickups: Int?,
    @SerializedName("location_name")
    val locationName: String?,
    val pickupLocation: PickupLocation?,
    @SerializedName("main_image")
    val mainImage: Image?,
    val images: List<Image>?,
    val properties: List<Property>?,
    val tags: List<Tag>?,
    val shipping: Int?,
    @SerializedName("shipping_options")
    val shippingOptions: List<ShippingOption>?,
    @SerializedName("combined_shipping")
    val combinedShipping: Int?,
    @SerializedName("start_price")
    val startPrice: Float?,

    // Mall marketplace
    @SerializedName("display_price")
    val displayPrice: Float?,
    @SerializedName("brand_new")
    val brandNew: Int?,
    @SerializedName("out_of_stock")
    val outOfStock: Boolean?,

    // Secondhand marketplace
    @SerializedName("buy_now")
    val buyNowPrice: Float?,
    @SerializedName("current_price")
    val currentPrice: Float?,
    val reserveMet: Boolean?,
    val noReserve: Boolean?,
    val expires: String?,
    @SerializedName("payment_options")
    val paymentOptions: List<PaymentOption>?
)