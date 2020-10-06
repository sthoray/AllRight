package com.sthoray.allright.data.api

import com.sthoray.allright.BuildConfig
import com.sthoray.allright.utils.Constants.Companion.BASE_API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit singleton class to make API requests from anywhere.
 */
class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            // Logging interceptor to make debugging easier.
            val logging = HttpLoggingInterceptor()
            val loggingLevel =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            logging.setLevel(loggingLevel)
            // OKHttp client with logging interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            // Finally, the retrofit builder with Gson converter for deserialization
            Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        /** The actual API service object that we can use for requests. */
        val api: AllGoodsApi by lazy {
            retrofit.create(AllGoodsApi::class.java)
        }
    }
}
