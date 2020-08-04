package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.listing.Category
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
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

    /** Top level categories response. */
    val topLevelCategories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

    /** Make network requests on initialisation. */
    init {
        getFeaturedCategories()
        getSecondTierCategories()
    }

    fun getFeaturedCategories() = viewModelScope.launch {
        safeGetFeaturedCategoriesCall()
    }

    fun getSecondTierCategories() = viewModelScope.launch {
        safeGetSecondTierCategories()
    }

    private suspend fun safeGetFeaturedCategoriesCall() {
        featureCategories.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getFeatureCategories()
                featureCategories.postValue(handleFeatureCategoriesResponse(response))
            } else {
                featureCategories.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> featureCategories.postValue(Resource.Error("Network Failure"))
                else -> featureCategories.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeGetSecondTierCategories() {
        topLevelCategories.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getSecondTierCategories()
                topLevelCategories.postValue(handleTopLevelCategoriesResponse(response))
            } else {
                topLevelCategories.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> topLevelCategories.postValue(Resource.Error("Network Failure"))
                else -> topLevelCategories.postValue(Resource.Error("Conversion Error"))
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

    private fun handleTopLevelCategoriesResponse(
        response: Response<List<Category>>
    ): Resource<List<Category>> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }

}