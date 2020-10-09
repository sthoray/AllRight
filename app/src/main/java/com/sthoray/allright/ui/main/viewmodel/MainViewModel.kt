package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.CategorySmall
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.user.User
import com.sthoray.allright.data.model.user.UserResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.EspressoIdlingResource
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * View model for the main activity.
 *
 * Stores data for the fragments contained in this activity. This is a work
 * around to prevent data being fetched every time a fragment is switched to.
 */
class MainViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    /** Featured categories response. */
    val featureCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> = MutableLiveData()

    /** Second tier categories response. */
    val secondTierCategories: MutableLiveData<Resource<List<CategorySmall>>> = MutableLiveData()

    /** User profile resource. */
    val userProfile: MutableLiveData<Resource<User>> = MutableLiveData()

    /** Flag to check if login state has changed. */
    var userProfileShown = false

    /** Make network requests on initialisation. */
    init {



        getFeaturedCategories()
        getSecondTierCategories()
        getUserProfile()


    }

    /** Refresh data used in the HomeFragment. */
    fun refreshHomeFragment() {
        getFeaturedCategories()
    }

    /** Refresh data used in the BrowseFragment. */
    fun refreshBrowseFragment() {
        getSecondTierCategories()
    }

    private fun getFeaturedCategories() = viewModelScope.launch {
        safeGetFeaturedCategoriesCall()
    }

    private fun getSecondTierCategories() = viewModelScope.launch {
        safeGetSecondTierCategories()
    }

    /**
     * Get the profile for the logged in user. If no user is logged-in, then
     * the [userProfile] data will be set to null.
     */
    fun getUserProfile() = viewModelScope.launch {
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

        val bearerToken = encryptedSharedPreferences.getString(
            getApplication<Application>().getString(R.string.user_bearer_token_key),
            null
        )

        // Check if a user is logged in
        if (bearerToken.isNullOrEmpty()) {
            userProfile.postValue(Resource.Success(null))
        } else {
            safeGetUserProfile(bearerToken)
        }
    }

    private suspend fun safeGetFeaturedCategoriesCall() {
        featureCategories.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getFeatureCategories()
                featureCategories.postValue(handleFeatureCategoriesResponse(response))
            } else {
                featureCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> featureCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> featureCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private suspend fun safeGetSecondTierCategories() {
        secondTierCategories.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getSecondTierCategories()
                secondTierCategories.postValue(handleSecondTierCategoriesResponse(response))

            } else {
                secondTierCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> secondTierCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> secondTierCategories.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private suspend fun safeGetUserProfile(bearerToken: String) {
        userProfile.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getUserProfile(bearerToken)
                userProfile.postValue(handleUserProfileResponse(response))

            } else {
                userProfile.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userProfile.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> userProfile.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private fun handleFeatureCategoriesResponse(
        response: Response<FeatureCategoriesResponse>
    ): Resource<FeatureCategoriesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSecondTierCategoriesResponse(
        response: Response<List<CategorySmall>>
    ): Resource<List<CategorySmall>> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUserProfileResponse(
        response: Response<UserResponse>
    ): Resource<User> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody.user)
            }
        } else if (response.code() == 401) {
            return Resource.Error(
                getApplication<Application>().getString(R.string.invalid_token_error)
            )
        }
        return Resource.Error(response.message())
    }
}
