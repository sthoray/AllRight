package com.sthoray.allright.data.model

/**
 * Model for search responses.
 *
 * Each page contains up to 24 [SearchItem]s.
 *
 * @property data object containing a list of items matching the search request
 * @property meta object containing the search metadata
 */
data class SearchResponse(
    val data: List<SearchItem>,
    val meta: SearchMeta
)