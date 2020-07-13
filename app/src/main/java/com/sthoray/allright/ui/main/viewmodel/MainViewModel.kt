package com.sthoray.allright.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * View model for the Main activity.
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property appRepository the data repository to interact with
 */
class MainViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    /** Featured categories response. */
    val featureCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> =
        MutableLiveData()

    /** Make the network request on initialisation. */
    init {
        getFeaturedCategories()
    }

    /** Get feature categories on a background thread. */
    private fun getFeaturedCategories() = viewModelScope.launch {
        featureCategories.postValue(Resource.Loading())
        val response = appRepository.getFeatureCategories()
        featureCategories.postValue(handleFeatureCategoriesResponse(response))
    }

    /**
     * Emit successful or failed responses.
     *
     * @param response the network response to handle
     *
     * @return a [Resource] containing data or an error message
     */
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