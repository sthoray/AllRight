package com.sthoray.allright.data.model.browse


import com.google.gson.annotations.SerializedName

/**
 * Browse response data model.
 *
 * @property category Details about the category that was browsed.
 * @property parents A list of parent categories. This will contain categories at each different level.
 * @property path An ordered list of the categories that lead to the the current [category]. The last category in this list will be the current [category].
 * @property related A list of categories that are related to the current items. These may also be of interest to the user even though they are not direct subcategories.
 * @property featuredPanels A list of featured categories to browse next. This contains less categories than [children] but each of these contain fancy images. This would be suitable for displaying at the top of search results.
 * @property children Children contained under this [category].
 */
data class BrowseResponse(
    val category: Category?,
    val parents: List<CategoryParent>?,
    val path: List<CategoryPath>?,
    val related: List<CategoryRelated>?,
    @SerializedName("featured_panels")
    val featuredPanels: List<FeaturedPanel>?,
    val children: List<CategoryChild>?
)