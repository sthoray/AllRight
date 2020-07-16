package com.sthoray.allright.utils

/**
 * The order to return search listings.
 *
 * @property key the AllGoods API sort key
 */
enum class SortOrder(val key: String) {
    
    BEST("best_match"),
    TRENDING("most_popular"),
    NEW_LISTINGS("created_at"),
    CLOSING_SOON("closing_soon"),
    MOST_BIDS("most_bids"),
    BUY_NOW_HIGHEST_FIRST("highest_buy_now"),
    BUY_NOW_LOWEST_FIRST("lowest_buy_now"),
    PRICE_LOWEST_FIRST("lowest_to_highest"),
    PRICE_HIGHEST_FIRST("highest_to_lowest"),
    A_TO_Z("name")

}