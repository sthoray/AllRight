package com.sthoray.allright.data.model.user

/**
 * Model for authentication.
 *
 * This object must be posted when authenticating using AllGoods credentials.
 *
 * @property email The user's email tied to an AllGoods account.
 * @property password The user's private password for their AllGoods account.
 */
data class Authentication(
    val email: String,
    val password: String
)
