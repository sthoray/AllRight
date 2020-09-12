package com.sthoray.allright.ui.login.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _loginResult.postValue(Resource.Error("Network failure"))
                else -> _loginResult.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private fun handleLoginResponse(
        response: Response<AuthenticationResponse>
    ): Resource<AuthenticationResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        } else if (response.code() == 401) {
            response.body()?.let {
                return Resource.Error(it.error!!)
            }
        }
        return Resource.Error(response.message())
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