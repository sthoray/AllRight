package com.sthoray.allright.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Builder object to manage HTTP requests.
 */
object RetrofitBuilder {

    /** The base url for the AllGoods API. */
    private const val BASE_URL = "https://allgoods.co.nz/api/"

    /**
     * Get a Retrofit builder object with a base url and Gson converter.
     *
     * @return a Retrofit Builder object
     */
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** ApiService object to interact with the AllGoods API. */
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}