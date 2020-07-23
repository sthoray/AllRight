package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.shared.Category
import com.sthoray.allright.data.model.shared.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import retrofit2.Response
import retrofit2.http.*

/** Defines requests for interacting with the AllGoods API. */
interface AllGoodsApi {

    /**
     * Get the category feature panel.
     *
     * @return A [FeatureCategoriesResponse] response
     */
    @GET("categoryFeaturePanel")
    suspend fun getFeatureCategories(): Response<FeatureCategoriesResponse>

    /**
     * Get the top level categories.
     *
     * @return A list of [Category]s.
     */
    @GET("category/topLevel")
    suspend fun getTopLevelCategories(): Response<List<Category>>

    /**
     * Search for listings.
     *
     * @param searchRequest A search request object.
     *
     * @return A [SearchResponse] response.
     */
    @POST("search")
    suspend fun searchForListings(@Body searchRequest: SearchRequest = SearchRequest()): Response<SearchResponse>

    /**
     * Get all information about a listing.
     *
     * @param listingId The id of the [Listing] to fetch.
     *
     * @return A [Listing] response.
     */
    @GET("product/{productId}")
    suspend fun getListing(@Path("productId") listingId: Int): Response<Listing>

}