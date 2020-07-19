package com.sthoray.allright.ui.fragments.home.viewmodel

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
class HomeViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    /** Featured categories response. */
    val featureCategories: MutableLiveData<Resource<FeatureCategoriesResponse>> =
        MutableLiveData()

    /** Make the network request on initialisation. */
    init {
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