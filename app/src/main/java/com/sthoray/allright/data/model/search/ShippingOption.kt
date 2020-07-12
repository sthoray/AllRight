package com.sthoray.allright.data.model.search

/**
 * Model for shipping options.
 *
 * This model (currently) excludes some properties.
 *
 * @property description the shipping option's description
 * @property cost the shipping option's cost
 */
data class ShippingOption(
    val description: String,
    val cost: Float
)
