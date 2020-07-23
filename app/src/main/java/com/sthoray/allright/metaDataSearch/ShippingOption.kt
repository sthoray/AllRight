package com.sthoray.allright.metaDataSearch

data class ShippingOption(
    val `class`: Int,
    val cost: Int,
    val created_at: String,
    val description: String,
    val display: Int,
    val id: Int,
    val name: String,
    val owner: Int,
    val pivot: Pivot,
    val updated_at: String
)