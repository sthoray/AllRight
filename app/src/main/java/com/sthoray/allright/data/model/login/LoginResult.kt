package com.sthoray.allright.data.model.login

import com.sthoray.allright.data.model.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)