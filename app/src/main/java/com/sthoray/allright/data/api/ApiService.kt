package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.CategoryFeaturePanel
import retrofit2.http.GET

/**
 * Interface for interacting with the AllGoods API.
 */
interface ApiService {

    /**
     * Get featured categories.
     *
     * @return a [CategoryFeaturePanel] object
     */
    @GET("categoryFeaturePanel")
    suspend fun getCategoryFeaturePanel(): CategoryFeaturePanel
}