package com.sthoray.allright.data.model.search

import com.google.gson.annotations.SerializedName

/**
 * Model for pagination info.
 *
 * @property total the number of listings in this search query (conflict's the reported number!)
 * @property count the number of listings in this page
 * @property perPage the maximum number of listings in each page
 * @property currentPage the current page number
 * @property totalPages the total number of pages in this search query
 */
data class Pagination(
    val total: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)