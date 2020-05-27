package com.sthoray.allright

// Search request

/**
 * Search request model.
 *
 * Contains properties for search POST requests. This object must be parsed to a
 * json string before being used in a request.
 */
class SearchRequest(var auctions: Int,
                    var brand_new: Int,
                    var category_id: String,
                    var fast_shipping: Int,
                    var free_shipping: Int,
                    var location: SearchLocation,
                    var page: Int,
                    var products: Int,
                    var propertyFilters: List<PropertyFilter>,
                    var showRestricted: Boolean,
                    var sort_by: String,
                    var useRegion: Boolean) {
    /**
     * Category search constructor.
     *
     * Create a standard search request object for a category. All filters are set
     * to off.
     *
     * @param category_id the category ID to search
     */
    constructor(category_id: String) : this(0,
        0,
        category_id,
        0,
        0,
        SearchLocation(),
        1,
        1,
        emptyList(),
        true,
        "best_match",
        false
    )
}

/**
 * Search location model.
 */
class SearchLocation()

/**
 * Property filter model.
 */
class PropertyFilter()
