package com.sthoray.allright.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * View model for the main activity.
 *
 * Stores data for the fragments contained in this activity. This is a work
 * around to prevent data being fetched every time a fragment is switched to.
 */
class MainViewModel(
    val appRepository: AppRepository
) : ViewModel() {


    /** Featured categories response. */
    val featureCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> = MutableLiveData()

    // TODO: Change this to a suitable data model
    /** Top level categories response. */
    val topLevelCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> = MutableLiveData()

    /** Make network requests on initialisation. */
    init {
        getFeaturedCategories()
        getTopLevelCategories()
    }

    private fun getFeaturedCategories() = viewModelScope.launch {
        featureCategories.postValue(Resource.Loading())
        val response = appRepository.getFeatureCategories()
        featureCategories.postValue(handleFeatureCategoriesResponse(response))
    }

    private fun getTopLevelCategories() = viewModelScope.launch {
        topLevelCategories.postValue(Resource.Loading())
        // TODO Change this to an appropriate method
        val response = appRepository.getFeatureCategories()
        topLevelCategories.postValue(handleTopLevelCategoriesResponse(response))
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

    // TODO Change this to use suitable data models
    private fun handleTopLevelCategoriesResponse(
        response: Response<FeatureCategoriesResponse>
    ): Resource<FeatureCategoriesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }
}