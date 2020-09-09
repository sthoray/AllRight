package com.sthoray.allright.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Model for user response.
 *
 * @property userData Contains all information about the authenticated user.
 */
data class UserResponse(
    @SerializedName("data")
    val userData: UserData?
)
