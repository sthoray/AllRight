package com.sthoray.allright.data.model.main

import com.sthoray.allright.data.model.listing.Category

/**
 * Model representing a category feature panel.
 *
 * AllGoods are using integer object names for each category. This can be
 * represented as a map containing indices and [Category]s. It may
 * be useful to convert [categories] into a List: E.g.
 * myObj.categories.values.toList()
 *
 * @property categories a map with [Category] as values
 */
data class FeatureCategoriesResponse(
    val categories: Map<Int, Category>
)