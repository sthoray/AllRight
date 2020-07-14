package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
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
     * @return a [FeatureCategoriesResponse] response
     */
    @GET("categoryFeaturePanel")
    suspend fun getFeatureCategories(): Response<FeatureCategoriesResponse>

    /**
     * Search for listings.
     *
     * @param searchRequest a search request object
     *
     * @return a [SearchResponse] response
     */
    @POST("search")
    suspend fun searchForListings(@Body searchRequest: SearchRequest = SearchRequest()): Response<SearchResponse>
}