package com.sthoray.allright.data.model

/**
 * Model representing a feature category panel.
 *
 * AllGoods are using integer object names for each category. This can be
 * represented as a map containing indices and [FeatureCategory]s. It may
 * be useful to convert [categories] into a List: E.g.
 * myObj.categories.values.toList()
 *
 * @property categories a map with [FeatureCategory] as values
 */
data class CategoryFeaturePanel(val categories: Map<Int, FeatureCategory>)