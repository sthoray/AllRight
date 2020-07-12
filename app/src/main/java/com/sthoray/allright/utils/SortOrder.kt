package com.sthoray.allright.utils

/**
 * The order to return search listings.
 *
 * @property key the AllGoods API sort key
 */
enum class SortOrder(val key: String) {

    /** Sort listings by best match. */
    BEST("best_match")
}