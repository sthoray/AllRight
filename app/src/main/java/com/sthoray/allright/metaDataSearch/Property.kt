package com.sthoray.allright.metaDataSearch

data class Property(
    val category_propertie_id: Int,
    val category_property: CategoryProperty,
    val created_at: Any,
    val id: Int,
    val multiple_products: Int,
    val options: Options,
    val product_id: Int,
    val type: Int,
    val updated_at: Any,
    val value: String
)