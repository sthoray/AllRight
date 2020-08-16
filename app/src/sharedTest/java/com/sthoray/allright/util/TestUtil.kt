package com.sthoray.allright.util

import com.sthoray.allright.data.model.search.SearchRequest

/**
 * Utilities to construct objects with sensible defaults for testing.
 */
class TestUtil {
    companion object {

        /**
         * Create a [SearchRequest] object for testing purposes.
         *
         * @return a [SearchRequest] with sane default values.
         */
        fun createSearchRequest(): SearchRequest {
            return SearchRequest()
        }
    }
}