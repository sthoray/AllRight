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
         * Reset the existing retrofit and api instances.
         *
         * Can be used to change the baseUrl of the Retrofit builder when performing tests.
         * It would probably be beneficial to refactor the [RetrofitInstance] class to better
         * support tests without these extra requirements.
         */
        fun resetInstance() {
            retrofitInstance = null
            apiInstance = null
        }

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

        private var retrofitInstance: Retrofit? = null

        private val retrofit: Retrofit
            get() {
                if (retrofitInstance == null) {
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
                    retrofitInstance = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
                }
                return retrofitInstance!!
            }

        private var apiInstance: AllGoodsApi? = null

        /**
         * The actual API service object for making network requests.
         *
         * Implements the [AllGoodsApi] interface.
         */
        val api: AllGoodsApi
            get() {
                if (apiInstance == null) {
                    apiInstance = retrofit.create(AllGoodsApi::class.java)
                }
                return apiInstance!!
            }
    }
}
