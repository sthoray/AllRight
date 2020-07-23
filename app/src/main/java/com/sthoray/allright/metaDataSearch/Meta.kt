package com.sthoray.allright.metaDataSearch

data class Meta(
    val categories: List<CategoryX>,
    val metaSearch: MetaSearch,
    val pagination: Pagination,
    val properties: List<Any>,
    val stores: List<Any>
)