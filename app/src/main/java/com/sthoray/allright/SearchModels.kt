package com.sthoray.allright

// Search request

/**
 * Search request model.
 *
 * Contains properties for search POST requests. This object must be parsed to a
 * json string before being used in a request.
 * TODO: Document properties
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
     * Create a standard category search request object. All filters are
     * disabled/default.
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
 * TODO: Create search location model
 */
class SearchLocation()

/**
 * Property filter model.
 * TODO: Create property filter search model
 * TODO: This could be the same as [SearchProperties], check and merge if possible
 */
class PropertyFilter()




// Search response

/**
 * Search response model.
 *
 * Each page contains up to 24 [SearchItem]s.
 *
 * @property data object containing a list of search results for the current page
 * @property meta object containing the search metadata
 */
class SearchResponse(val data: Array<SearchItem>, val meta: SearchMeta)


// Search Response META

/**
 * Search meta model.
 * TODO: Document search meta properties
 */
class SearchMeta(val properties: List<SearchProperties>,
                 val categories: List<SearchCategories>,
                 val stores: List<SearchStores>,
                 val metaSearch: MetaSearch,
                 val pagination: SearchPagination)

/**
 * Search properties.
 * TODO: Create search property model.
 */
class SearchProperties()

/**
 * Search categories.
 * TODO: Create search categories model.
 * TODO: Investigate the overlap between category models and merge if possible.
 */
class SearchCategories()

/**
 * Search stores.
 * TODO: Create search store model.
 */
class SearchStores()

/**
 * Meta search model.
 * TODO: Create meta search model.
 */
class MetaSearch()

/**
 * Search pagination model.
 * TODO: Create search pagination model.
 */
class SearchPagination()


// Search Response DATA

/**
 * Search item model.
 *
 * This model (currently) does not contain all possible properties. As a general rule, we
 * should process as little information as possible. Properties may not be used in both
 * "Mall" and "Second hand" marketplaces. Differences also exist between categories. This
 * is why some properties are nullable.
 * TODO: Document search data properties
 */
class SearchItem(val id: Int,
                 val name: String,
                 val location_name: String,
                 val start_price: Float?,
                 val buy_now: Float?,
                 val current_price: Float?,
                 val shipping: Int, // TODO: Decode what each 'shipping' number represents
                 val shipping_options: List<ShippingOption>,
                 val main_image: MainImage)

/**
 * Shipping option model.
 *
 * This model (currently) excludes some properties.
 * TODO: Document search data properties
 */
class ShippingOption(val description: String, val cost: Float)

/**
 * Main image model.
 *
 * Contains urls for a items main image and its thumbnail.
 *
 * @property large_url the fully qualified url to the main image
 * @property thumb_url the fully qualified url to the thumbnail
 */
class MainImage(val large_url: String, val thumb_url: String)