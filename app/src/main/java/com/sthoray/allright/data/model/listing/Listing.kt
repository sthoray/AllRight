package com.sthoray.allright.data.model.listing



import com.google.gson.annotations.SerializedName

/**
 * Model for the product or listing or item.
 *
 * There is quite a few properties, some properties might not be necessary.
 * 
 * @property id the id of the listing
 * @property shipping the type of shipping offered
 * @property startPrice the starting price of the listing
 * @property buyNow the buy now price of the listing
 * @property currentPrice the current auction price of the listing
 * @property listingMainImage the main image of the listing, which has its own data class.
 * @property productName the name of the listing
 * @property locationName the location name of the listing
 * @property listingRelated list of [ListingRelated].
 * @property storeRelated list of [StoreRelated].
 * @property listingDescription the description of  the product or listing
 * @property listingStats is a class for product stats.
 * @property shippingOptions a list of [ShippingOption]s
 * @property listingCategories a list of [ListingCategory].
 * @property brandTitle the title of the brand for the product.
 * @property checkInStock to check if the item is in stock.
 * @property checkReserveMet to check if the reserve price has met.
 * @property checkReservePrice to check if the product has a reserve price.
 * @property listingManager a class for the product manager.
 * @property listingPickupLocation location for the product pickup.
 * @property listingImages details about all images related to the product.
 */
data class Listing(
    val id: Int,
    val shipping: Int,
    @SerializedName("start_price")
    val startPrice: Float?,
    @SerializedName("buy_now")
    val buyNow: Float?,
    @SerializedName("current_price")
    val currentPrice: Float?,
    @SerializedName("main_image")
    val listingMainImage: ListingMainImage,
    @SerializedName("name")
    val productName: String,
    @SerializedName("location_name")
    val locationName: String,
    @SerializedName("related")
    val listingRelated: List<ListingRelated>,
    @SerializedName("store_related")
    val storeRelated: List<StoreRelated>,

    @SerializedName("description")
    val listingDescription: String,
    @SerializedName("stats")
    val listingStats: ListingStats,
    @SerializedName("shipping_options")
    val shippingOptions: List<ShippingOption>,
    @SerializedName("categories")
    val listingCategories: List<ListingCategory>,
    @SerializedName("brand_title")
    val brandTitle: String,
    @SerializedName("out_of_stock")
    val checkInStock: Boolean,
    @SerializedName("reserve_met")
    val checkReserveMet: Boolean,
    @SerializedName("no_reserve")
    val checkReservePrice: Boolean,
    @SerializedName("manager")
    val listingManager: ListingManager,
    @SerializedName("pickupLocation")
    val listingPickupLocation: ListingPickupLocation,

    @SerializedName("images")
    val listingImages: List<ListingImage>
)