package com.sthoray.allright.data.model.search

/**
 * Model for search responses.
 *
 * Each page contains up to 24 [Listing]s.
 *
 * @property data list containing listings
 * @property meta the search response metadata
 */
data class SearchResponse(
    val data: List<Listing>,
    val meta: SearchResponseMetadata
)
