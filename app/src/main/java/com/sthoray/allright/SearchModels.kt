package com.sthoray.allright

// Search request

/**
 * Search request model.
 *
 * Contains properties for search POST requests. This object must be parsed to a
 * json string before being used in a request.
 *
 * @property auctions 1 if auctions should be included, else 0
 * @property brand_new 1 if only brand new should be included, else 0
 * @property category_id the ID of the category to search in, else 0 to ignore
 * @property fast_shipping 1 if items should have fast shipping, else 0
 * @property free_shipping 1 if items should have free shipping, else 0
 * @property location the [SearchLocation] to search around
 * @property page the page to get items from for the current search properties
 * @property products 1 if store 'products' should be included, else 0
 * @property propertyFilters a list of search [PropertyFilter]s
 * @property showRestricted true if restricted items should be included, else false
 * @property sort_by the sort order of the items TODO: create an enum for sort_by
 * @property useRegion the region to return results from
 */
class SearchRequest(var auctions: Int,
                    var brand_new: Int,
                    var category_id: Int,
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
     * Empty search constructor.
     *
     * Create a standard search request object with no filters enabled.
     */
    constructor() : this(
        0,
        0,
        0,
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

    /**
     * Category search constructor.
     *
     * Create a standard category search request object. All filters are
     * disabled/default.
     *
     * @param category_id the ID of the category to search in
     */
    constructor(category_id: Int) : this(
        0,
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

    /**
     * Change between the AllGoods 'Mall' and 'Second Hand' marketplace.
     *
     * This inverts the [auctions] and [products] properties of this search request.
     */
    fun toggleCategory(){
        auctions = auctions xor 1
        products = products xor 1
        // TODO change xor to call to inv without crashing the app
        // TODO cont: the app crashes when calling inv like: auctions = auctions.inv()
    }
}

/**
 * Search location model.
 * TODO: Create search location model
 */
class SearchLocation()

/**
 * Property filter model.
 * TODO: Create property filter search model. Possibly merge with [SearchProperties]?
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
 *
 * @property properties a list of [SearchProperties]s from a search result
 * @property categories a list of [SearchCategories]s from a search result
 * @property stores a list of [SearchStores] contained in a search result
 * @property metaSearch meta data from a search result
 * @property pagination pagination information from a search result
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
 * TODO: Create search categories model. Possible merge with other category models?
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
 *
 * @property total the number of listings in this search query [this conflict's the reported number!]
 * @property count the number of listings in this page
 * @property per_page the maximum number of listings in each page
 * @property current_page our page position in the pages
 * @property total_pages the total number of pages in this search query
 */
class SearchPagination(val total: Int,
                       val count: Int,
                       val per_page: Int,
                       val current_page: Int,
                       val total_pages: Int)


// Search Response DATA

/**
 * Search item model.
 *
 * This model (currently) does not contain all possible properties. As a general rule, we
 * should process as little information as possible. Properties may not be used in both
 * "Mall" and "Second hand" marketplaces. Differences also exist between categories. This
 * is why some properties are nullable.
 *
 * @property id the id of the listing
 * @property name the name of the listing
 * @property location_name the location name of the listing
 * @property start_price the starting price of the listing
 * @property buy_now the buy now price of the listing
 * @property current_price the current auction price of the listing
 * @property shipping the type of shipping offered // TODO: Decode what each 'shipping' number represents
 * @property shipping_options a list of [ShippingOption]s
 * @property main_image the main image of the listing
 */
class SearchItem(val id: Int,
                 val name: String,
                 val location_name: String,
                 val start_price: Float?,
                 val buy_now: Float?,
                 val current_price: Float?,
                 val shipping: Int,
                 val shipping_options: List<ShippingOption>,
                 val main_image: MainImage)

/**
 * Shipping option model.
 *
 * This model (currently) excludes some properties.
 *
 * @property description the description of the shipping option
 * @property cost the cost of the shipping option
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