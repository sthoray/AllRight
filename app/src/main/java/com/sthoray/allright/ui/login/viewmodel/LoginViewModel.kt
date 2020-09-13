package com.sthoray.allright.ui.login.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.sthoray.allright.R
import com.sthoray.allright.data.model.user.AuthenticationResponse
import com.sthoray.allright.data.repository.LoginRepository
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * View model for the login activity.
 */
class LoginViewModel(
    app: Application,
    private val loginRepository: LoginRepository
) : AndroidViewModel(app) {

    private val _loginForm = MutableLiveData<LoginFormState>()

    /** The current login form state. */
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Resource<AuthenticationResponse>>()

    /** The result from a login attempt. */
    val loginResult: LiveData<Resource<AuthenticationResponse>> = _loginResult

    /**
     * Attempt to login to AllGoods with an AllGoods account.
     *
     * @param username The user's AllGoods' username/email.
     * @param password The user's AllGoods' password.
     */
    fun login(username: String, password: String) = viewModelScope.launch {
        safeLogin(username, password)
    }

    private suspend fun safeLogin(username: String, password: String) {
        _loginResult.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = loginRepository.login(username, password)
                _loginResult.postValue(handleLoginResponse(response))
            } else {
                _loginResult.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _loginResult.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> _loginResult.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private fun handleLoginResponse(
        response: Response<AuthenticationResponse>
    ): Resource<AuthenticationResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                responseBody.token?.let { commitUserCredentials(it) }
                return Resource.Success(responseBody)
            }
        } else if (response.code() == 401) {
            // Custom error message from AllGoods API
            val errorBody = Gson().fromJson(
                response.errorBody()?.charStream(),
                AuthenticationResponse::class.java
            )
            errorBody.error?.let {
                if (it == "invalid_credentials") {
                    return Resource.Error(
                        getApplication<Application>().getString(R.string.invalid_credentials)
                    )
                }
                // Other error messages appear to be formatted in natural language
                return Resource.Error(it)
            }
        }
        // Fallback to response error message
        return Resource.Error(response.message())
    }

    private fun commitUserCredentials(bearerToken: String) {
        val masterKeyAlias = MasterKey.Builder(getApplication(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            getApplication(),
            getApplication<Application>().getString(R.string.preference_crypt_auth_key),
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        encryptedSharedPreferences.edit().apply {
            putString(
                getApplication<Application>().getString(R.string.user_bearer_token_key),
                bearerToken
            )
        }.apply()
    }

    /**
     * Submit a new username and password and perform validation checks.
     *
     * @param username The user's AllGoods' username/email.
     * @param password The user's AllGoods' password.
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // Username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // Password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }
}