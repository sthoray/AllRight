package com.sthoray.allright.metaDataProduct

data class ActiveOffer(
    val created_at: String,
    val fixed_shipping: Int,
    val free_shipping: Int,
    val id: Int,
    val name: String,
    val offer_option: String,
    val offer_type: Int,
    val percentage_discount: Int,
    val price_discount: Int,
    val price_required: Int,
    val promo_code: Any,
    val quantity_limit: Any,
    val quantity_required: Int,
    val rule_option: String,
    val shipping_discount: Int,
    val store_id: Int,
    val type: Int,
    val updated_at: String
)