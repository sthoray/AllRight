package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model representing a category.
 *
 * @property id The category ID.
 * @property name The name of the category.
 * @property level The tree level of the category. Second tier categories are level 1 while their children are level 2.
 * @property icon The name of the icon representing the category.
 * @property appIcon The name of the icon in the mobil app representing the category.
 * @property listingCount The number of listings contained in the category.
 * @property auctionCount The number of auctions contained in the category.
 * @property productCount The number of products contained in the category.
 * @property imagePath The url path to an associated image. This is useful for featured panels where an image is preferred over an icon. This is not present in some API calls.
 * @property isRestricted 1 if the the user must be at least 18 years old to browse the category, else 0 for no restrictions.
 * @property showGoodAsNew 1 if "Good as New" condition should be displayed in the secondhand marketplace.
 * @property defaultToFreeShipping 1 if the Search Request should enable "free shipping" by default, else 0.
 * @property defaultToFastShipping 1 if the Search Request should enable "fast shipping" by default, else 0.
 * @property defaultToBrandNew 1 if the Search Request should enable "brand new" by default, else 0.
 * @property defaultToListLayout 1 if the search results should be displayed in a list by default, else 0.
 * @property children The list of categories that are one level below this category. If the category is a child node, [children] will not be present.
 */
data class Category(
    val id: Int,
    val name: String,
    val level: Int?,
    val icon: String,
    @SerializedName("app_icon") val appIcon: String?,
    @SerializedName("listing_count") val listingCount: Int?,
    @SerializedName("auction_count") val auctionCount: Int?,
    @SerializedName("product_count") val productCount: Int?,
    @SerializedName("image") val imagePath: String?,
    @SerializedName("is_restricted") val isRestricted: Int?,
    @SerializedName("show_good_as_new_in_secondhand") val showGoodAsNew: Int?,
    @SerializedName("default_free_shipping") val defaultToFreeShipping: Int?,
    @SerializedName("default_fast_shipping") val defaultToFastShipping: Int?,
    @SerializedName("default_brand_new") val defaultToBrandNew: Int?,
    @SerializedName("default_layout_list") val defaultToListLayout: Int?,
    val children: List<Category>?
)