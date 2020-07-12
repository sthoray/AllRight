package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.main.CategoryFeaturePanelResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Defines requests for interacting with the AllGoods API.
 */
interface AllGoodsApi {

    /**
     * Get the category feature panel.
     *
     * @return a [CategoryFeaturePanelResponse] response
     */
    @GET("categoryFeaturePanel")
    suspend fun getCategoryFeaturePanel(): Response<CategoryFeaturePanelResponse>

    /**
     * Search for listings.
     *
     * [categoryId] and [pageNumber] could both be passed to the `SearchRequest`
     * constructor. Any property specified inside [searchRequest] will override
     * the corresponding search query parameters
     *
     * @param searchQuery the query to search for
     * @param categoryId the id of the category to search in
     * @param pageNumber the page to fetch
     * @param searchRequest a search request object
     *
     * @return a [SearchResponse] response
     */
    @POST("search")
    suspend fun search(
        @Query("q")
        searchQuery: String?,
        @Query("category_id")
        categoryId: Int? = 0,
        @Query("page")
        pageNumber: Int? = 1,
        @Body
        searchRequest: SearchRequest = SearchRequest(
            categoryId = null,
            pageNumber = null
        )
    ): Response<SearchResponse>
}
