package com.sthoray.allright.data.model

/**
 * Model for a shipping option.
 *
 * This model (currently) excludes some properties.
 *
 * @property description the description of the shipping option
 * @property cost the cost of the shipping option
 */
data class ShippingOption(
    val description: String,
    val cost: Float
)
