package com.sthoray.allright.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.listing.Category
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

    /** Top level categories response. */
    val topLevelCategories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

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
        val response = appRepository.getTopLevelCategories()
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