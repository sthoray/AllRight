package com.sthoray.allright.data.model.search

/**
 * Model for search response metadata.
 *
 * This model (currently) excludes some properties.
 *
 * @property pagination pagination information from a search response
 */
data class SearchResponseMetadata(
    val pagination: Pagination
)