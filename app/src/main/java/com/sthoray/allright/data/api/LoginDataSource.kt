package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.login.LoggedInUser
import com.sthoray.allright.utils.Resource
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Resource<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Resource.Success(fakeUser)
        } catch (e: Throwable) {
            return Resource.Error("Error logging in: " + e.message)
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}