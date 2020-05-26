package com.sthoray.allright

// Browse model

/**
 * Top level category model.
 *
 * Stores basic information relating to a top level category. The properties are
 * used to display a top level category in the browse activity.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property listing_count the number of listings contained in the category
 * @property icon the name of the icon to represent the category
 */
class TopLevelCategory(val id: Int, val name: String, val listing_count: Int, val icon: String)




// Discover models

/**
 * Featured category panel model.
 *
 * Contains a list of categories to display in the feature panel. AllGoods are
 * unfortunately using integers object names for each category. To work around
 * this the categories property is a Map. It may be useful to convert this
 * property into a List before using it. e.g. ArrayList(myObj.categories.values)
 *
 * @property categories a map with [FeatureCategory] values
 */
class FeaturePanelCategory(val categories: Map<Int, FeatureCategory>)

/**
 * Featured category model.
 *
 * Contains information required to display a featured category. The properties are
 * used to display a featured category in the discover activity.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property icon the name of the icon to represent the category
 * @property listing_count the number of listings contained in the category
 * @property image the url path to an associated image
 * @property url the url path to browse the category in a web browser
 */
class FeatureCategory(val id: Int,
                      val name: String,
                      val icon: String,
                      val listing_count: Int,
                      val image: String,
                      val url: String)




// Search models

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
