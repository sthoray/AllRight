package com.sthoray.allright.data.api

import com.sthoray.allright.BuildConfig
import com.sthoray.allright.utils.Constants.Companion.BASE_API_URL
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit singleton class to make API requests from anywhere.
 */
class RetrofitInstance {
    companion object {

        /**
         * The base url for the retrofit builder.
         *
         * If this variable is modified before the retrofit builder is created, i.e. when
         * [RetrofitInstance.api] is first accessed, then the modified baseUrl will be used.
         * If the retrofit builder object has already been initialised, then modifying this
         * variable will have no affect on retrofit calls. This should be set to a
         * `mockWebServer.url("/")` when mocking retrofit network calls in tests. We
         * recommend making this variable private in a later refactor.
         */
        var baseUrl: HttpUrl = BASE_API_URL.toHttpUrl()

        private val retrofit by lazy {
            // Logging interceptor to make debugging easier.
            val logging = HttpLoggingInterceptor()
            logging.setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )

            // OKHttp client with logging interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            // Finally, the retrofit builder with Gson converter for deserialization
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        /**
         * The actual API service object for making network requests.
         *
         * Implements the [AllGoodsApi] interface.
         */
        val api: AllGoodsApi by lazy {
            retrofit.create(AllGoodsApi::class.java)
        }
    }
}
