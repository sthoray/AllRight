package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.browse.BrowseResponse
import com.sthoray.allright.data.model.listing.Category
import com.sthoray.allright.data.model.listing.CategorySmall
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.model.user.Authentication
import com.sthoray.allright.data.model.user.AuthenticationResponse
import com.sthoray.allright.data.model.user.UserResponse
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
     * Get the second tier categories.
     *
     * @return A list of [Category]s.
     */
    @GET("category/secondTier")
    suspend fun getSecondTierCategories(): Response<List<CategorySmall>>

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

    /**
     * Get a token for authentication.
     *
     * @param auth The users AllGoods login in details.
     *
     * @return An [AuthenticationResponse] object
     */
    @POST("authenticate")
    suspend fun login(@Body auth: Authentication): Response<AuthenticationResponse>

    /**
     * Get the current user's profile.
     *
     * It may be preferable to add the header using an interceptor.
     *
     * @param bearerToken The bearerToken provided by a successful [login] request.
     *
     * @return An [UserResponse] object
     */
    @GET("user")
    suspend fun getUserProfile(@Header("Authorization") bearerToken: String): Response<UserResponse>

    /**
     * Browse a given category.
     *
     * Returns with the provided category information along with other
     * related categories.
     *
     * @param categoryId The category ID to browse.
     * @param type The type of market place to search. Known values: 1 = mall, 2 = secondhand.
     *
     * @return A [BrowseResponse] object
     */
    @GET("category/{categoryId}/browse")
    suspend fun browseCategory(
        @Path("categoryId") categoryId: Int,
        @Query("type") type: Int
    ): Response<BrowseResponse>
}
