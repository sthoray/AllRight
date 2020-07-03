package com.sthoray.allright.data.model

/**
 * Model for search metadata.
 *
 * @property properties a list of [SearchProperties]s from a search response
 * @property categories a list of [SearchCategories]s from a search response
 * @property stores a list of [SearchStores] contained in a search response
 * @property metaSearch metadata from a search response
 * @property pagination pagination information from a search response
 */
data class SearchMeta(
    val properties: List<SearchProperties>,
    val categories: List<SearchCategories>,
    val stores: List<SearchStores>,
    val metaSearch: MetaSearch,
    val pagination: SearchPagination
)