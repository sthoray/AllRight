package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.CategoryFeaturePanel
import com.sthoray.allright.data.model.SearchRequest
import com.sthoray.allright.data.model.SearchResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface for interacting with the AllGoods API.
 */
interface ApiService {

    /**
     * Get featured categories panel.
     *
     * @return a [CategoryFeaturePanel]
     */
    @GET("categoryFeaturePanel")
    suspend fun getCategoryFeaturePanel(): CategoryFeaturePanel

    /**
     * Get search results for a given request.
     *
     * @param searchRequest a [SearchRequest] describing the search query
     *
     * @return the [SearchResponse] for the searchRequest
     */
    @POST("search")
    suspend fun search(@Body searchRequest: SearchRequest): SearchResponse
}