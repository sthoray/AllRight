package com.sthoray.allright.data.model.user

/**
 * Data model for an authentication response.
 *
 * If the user credentials were valid then the response (code 200) will
 * contain the users token. Invalid credentials will result with a 401
 * response and an error message instead of a token.
 *
 * @property error The reason for failed authentication, e.g. "invalid_credentials"
 * @property token The token to use for accessing secure user resources.
 */
data class AuthenticationResponse(
    val error: String?,
    val token: String?
)
