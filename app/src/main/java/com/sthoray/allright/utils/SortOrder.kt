package com.sthoray.allright.utils

/**
 * The order to return search listings.
 *
 * @property key The AllGoods API sort key.
 */
enum class SortOrder(val key: String) {
    BEST("best_match"),
    POPULAR("most_popular"),
    TRENDING("most_popular"),
    PRICE_SHIPPED_LOWEST("lowest_to_highest_w_shippping"),
    PRICE_LOWEST("lowest_to_highest"),
    PRICE_HIGHEST("highest_to_lowest"),
    BUY_NOW_LOWEST("lowest_buy_now"),
    BUY_NOW_HIGHEST("highest_buy_now"),
    NEW_PRODUCTS("created_at"),
    NEW_LISTINGS("created_at"),
    SALE("discount_percentage"),
    ALPHABETICAL("name"),
    CLOSING_SOON("closing_soon"),
    MOST_BIDS("most_bids")
}