package com.sthoray.allright.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model for a search request.
 *
 * Default values represent a search without any filters.
 *
 * @property auctions 1 if auctions are required, else 0
 * @property brandNew 1 if brand new items are required, else 0
 * @property categoryId the ID of the category to search in, else 0 to search everywhere
 * @property fastShipping 1 if fast shipping is required, else 0
 * @property freeShipping 1 if free shipping is required, else 0
 * @property location the [SearchLocation] to search around
 * @property page the page to get items from for the current search query
 * @property products 1 if store 'products' should be included, else 0
 * @property propertyFilters a list of search [PropertyFilter]s
 * @property showRestricted true if restricted items should be included, else false
 * @property sortBy the sort order of the returned results
 * @property useRegion the region to return results from
 */
data class SearchRequest(
    var auctions: Int = 0,
    @SerializedName("brand_new")
    var brandNew: Int = 0,
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("fast_shipping")
    var fastShipping: Int = 0,
    @SerializedName("free_shipping")
    var freeShipping: Int = 0,
    var location: SearchLocation = SearchLocation(),
    var page: Int = 1,
    var products: Int = 1,
    var propertyFilters: List<PropertyFilter> = emptyList(),
    var showRestricted: Boolean = true,
    @SerializedName("sort_by")
    var sortBy: String = Order.BEST.key,
    var useRegion: Boolean = false
)