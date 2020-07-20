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
    val featureCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> =
        MutableLiveData()

    /** Make network requests on initialisation. */
    init {
        Log.i("MainViewModel", "Init VM")
        getFeaturedCategories()
    }

    private fun getFeaturedCategories() = viewModelScope.launch {
        featureCategories.postValue(Resource.Loading())
        val response = appRepository.getFeatureCategories()
        featureCategories.postValue(handleFeatureCategoriesResponse(response))
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


}