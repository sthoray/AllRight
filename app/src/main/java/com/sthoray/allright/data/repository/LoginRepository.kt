package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.model.user.Authentication

/**
 * Class that requests authentication from the remote data source.
 */
class LoginRepository {

    /**
     * Attempt to login to AllGoods and return the token if successful.
     *
     * @param username The user's AllGoods username/email.
     * @param password The user's AllGoods password
     */
    suspend fun login(username: String, password: String) = RetrofitInstance.api.login(
        Authentication(
            email = username,
            password = password
        )
    )
}
