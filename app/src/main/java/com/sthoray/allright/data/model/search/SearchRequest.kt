package com.sthoray.allright.data.model.search

import com.google.gson.annotations.SerializedName
import com.sthoray.allright.utils.SortOrder

/**
 * Model for search requests.
 *
 * Default values represent a search without any filters. Nullable properties
 * must be specified when searching the AllGoods API without queries.
 *
 * @property searchQuery the query to search for
 * @property auctions 1 if auctions are required, else 0
 * @property brandNew 1 if brand new items are required, else 0
 * @property categoryId the id of the category to search in, `0` searches everywhere
 * @property fastShipping 1 if fast shipping is required, else 0
 * @property freeShipping 1 if free shipping is required, else 0
 * @property location the location to search around
 * @property pageNumber the page to fetch
 * @property products 1 if store 'products' should be included, else 0
 * @property propertyFilters a list of search property filters
 * @property showRestricted true if restricted items should be included, else false
 * @property sortBy the sort order of the returned results
 * @property useRegion the region to return results from
 */
data class SearchRequest(
    @SerializedName("q")
    var searchQuery: String = "",
    var auctions: Int = 0,
    @SerializedName("brand_new")
    var brandNew: Int = 0,
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("fast_shipping")
    var fastShipping: Int = 0,
    @SerializedName("free_shipping")
    var freeShipping: Int = 0,
    var location: Any = Any(),
    @SerializedName("max_price")
    var maxPrice: Float? = null,   // Client side casts max price and min price to an int
                             // but the server side accepts float values
    @SerializedName("min_price")
    var minPrice: Float? = null,
    @SerializedName("page")
    var pageNumber: Int = 1,
    var products: Int = 1,
    var propertyFilters: List<Any> = emptyList(),
    var showRestricted: Boolean = true,
    @SerializedName("sort_by")
    var sortBy: String = SortOrder.BEST.key,
    var useRegion: Boolean = false
)