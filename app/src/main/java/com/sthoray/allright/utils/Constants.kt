package com.sthoray.allright.utils

import android.os.Build

/**
 * Constants used in this project.
 */
class Constants {
    companion object {

        /** Base URL for the AllGoods website. */
        const val BASE_URL = "https://allgoods.co.nz/"

        /** Base URL for the AllGoods API. */
        const val BASE_API_URL = "${BASE_URL}api/"

        /** Base URL for the AllGoods API. */
        const val BASE_PRODUCT_URL = "${BASE_URL}product/"

        fun getVersionSDKInt(): Int {
            return Build.VERSION.SDK_INT
        }
    }
}