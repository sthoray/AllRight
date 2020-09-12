package com.sthoray.allright.ui.login.viewmodel

import com.sthoray.allright.data.model.user.AuthenticationResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: AuthenticationResponse? = null,
    val error: Int? = null
)