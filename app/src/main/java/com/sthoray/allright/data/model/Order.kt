package com.sthoray.allright.data.model

/**
 * The order to sort results by in a search response.
 *
 * @property key the AllGoods API sort string
 */
enum class Order(val key: String) {

    /** Sort listings by best match. */
    BEST("best_match")
}