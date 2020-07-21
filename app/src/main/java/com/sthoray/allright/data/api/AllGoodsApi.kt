package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.browse.TopLevelCategory
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import retrofit2.Response
import retrofit2.http.*

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
     * Get the top level categories.
     *
     * @return a list of [TopLevelCategory]s
     */
    @GET("category/topLevel")
    suspend fun getTopLevelCategories(): Response<List<TopLevelCategory>>

    /**
     * Search for listings.
     *
     * @param searchRequest a search request object
     *
     * @return a [SearchResponse] response
     */
    @POST("search")
    suspend fun searchForListings(@Body searchRequest: SearchRequest = SearchRequest()): Response<SearchResponse>

    /**
     * Get listing of that id.
     *
     * @param listingId id of the product.
     * @return a [Listing] response.
     */
    @GET("product/{productId}")
    suspend fun getListing(@Path("productId") listingId: Int): Response<Listing>

}