package com.sthoray.allright.data.model.listing

/**
 * Model for shipping options.
 *
 * This model excludes some properties.
 *
 * @property id The shipping option's ID.
 * @property description The shipping option's description.
 * @property cost The shipping option's cost.
 */
data class ShippingOption(
    val id: Int?,
    val description: String?,
    val cost: Float?
)