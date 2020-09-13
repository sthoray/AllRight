package com.sthoray.allright.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.data.repository.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {

    /**
     * Create a new instance of LoginViewModel
     *
     * @param modelClass The ViewModel class whose instance is requested.
     * @param T The type parameter for the ViewModel.
     *
     * @return T a newly created ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                app = app,
                loginRepository = LoginRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
