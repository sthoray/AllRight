package com.sthoray.allright.data.model.listing

/**
 * Model for a shipping option.
 * Still some attributes missing.
 * @property id the id.
 * @property name the name.
 * @property description the description of the shipping option
 * @property cost the cost of the shipping option
 */
data class ShippingOption(
    val id: Int,
    val name: String,
    val description: String,
    val cost: Float
)