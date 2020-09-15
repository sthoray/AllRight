package com.sthoray.allright.data.model.search

import com.sthoray.allright.data.model.listing.Category

/**
 * Model for search response metadata.
 *
 * This model (currently) excludes some properties.
 *
 * @property pagination Pagination information from a search response.
 * @property categories The list of subcategories for the current search request containing listings.
 */
data class SearchResponseMetadata(
    val pagination: Pagination,
    val categories: List<Category>?
)