package com.sthoray.allright.data.model.listing

/**
 * Model for payment options for secondhand listings.
 *
 * @property id The payment option ID.
 * @property name The description of the payment option, e.g. "Cash"
 */
data class PaymentOption(
    val id: Int?,
    val name: String?
)