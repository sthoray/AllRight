package com.sthoray.allright.utils

import com.sthoray.allright.R

/**
 * The order to return search listings.
 *
 * @property key The AllGoods API sort key.
 * @property resourceId The ID of the string resource to display in the view.
 */
enum class SortOrder(val key: String, val resourceId: Int) {
    BEST(
        "best_match",
        R.string.sort_by_best
    ),
    POPULAR(
        "most_popular",
        R.string.sort_by_popular
    ),
    TRENDING(
        "most_popular",
        R.string.sort_by_trending
    ),
    PRICE_SHIPPED_LOWEST(
        "lowest_to_highest_w_shippping",
        R.string.sort_by_price_shipped_lowest
    ),
    PRICE_LOWEST(
        "lowest_to_highest",
        R.string.sort_by_price_lowest
    ),
    PRICE_HIGHEST(
        "highest_to_lowest",
        R.string.sort_by_price_highest
    ),
    BUY_NOW_LOWEST(
        "lowest_buy_now",
        R.string.sort_by_buy_now_lowest
    ),
    BUY_NOW_HIGHEST(
        "highest_buy_now",
        R.string.sort_by_buy_now_highest
    ),
    NEW_PRODUCTS(
        "created_at",
        R.string.sort_by_new_products
    ),
    NEW_LISTINGS(
        "created_at",
        R.string.sort_by_new_listings
    ),
    SALE(
        "discount_percentage",
        R.string.sort_by_sale
    ),
    ALPHABETICAL(
        "name",
        R.string.sort_by_alphabetical
    ),
    CLOSING_SOON(
        "closing_soon",
        R.string.sort_by_closing_soon
    ),
    MOST_BIDS(
        "most_bids",
        R.string.sort_by_most_bids
    )
}