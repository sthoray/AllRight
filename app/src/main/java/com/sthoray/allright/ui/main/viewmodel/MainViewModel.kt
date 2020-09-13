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

    /** Second tier categories response. */
    val secondTierCategories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

    /** Make network requests on initialisation. */
    init {
        getFeaturedCategories()
        getSecondTierCategories()
    }

    fun getRefreshFragment(){
        getFeaturedCategories()
    }

    private fun getFeaturedCategories() = viewModelScope.launch {
        safeGetFeaturedCategoriesCall()
    }

    private fun getSecondTierCategories() = viewModelScope.launch {
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
        secondTierCategories.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getSecondTierCategories()
                secondTierCategories.postValue(handleSecondTierCategoriesResponse(response))

            } else {
                secondTierCategories.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> secondTierCategories.postValue(Resource.Error("Network Failure"))
                else -> secondTierCategories.postValue(Resource.Error("Conversion Error"))
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