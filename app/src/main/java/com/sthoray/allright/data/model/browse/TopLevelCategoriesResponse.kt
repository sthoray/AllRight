package com.sthoray.allright.data.model.browse

/**
 * Model representing a top level categories response.
 *
 * @property categories a list of [TopLevelCategory]s
 */
data class TopLevelCategoriesResponse(
    val categories: List<TopLevelCategory>
)