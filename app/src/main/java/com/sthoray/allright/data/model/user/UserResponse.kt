package com.sthoray.allright.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Model for user response.
 *
 * @property user Contains all information about the authenticated user.
 */
data class UserResponse(
    @SerializedName("data")
    val user: User?
)
