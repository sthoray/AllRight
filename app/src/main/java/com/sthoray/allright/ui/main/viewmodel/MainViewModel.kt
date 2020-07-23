package com.sthoray.allright.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.browse.TopLevelCategory
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.repository.AppRepository
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
    val topLevelCategories: MutableLiveData<Resource<List<TopLevelCategory>>> = MutableLiveData()

    /** Make network requests on initialisation. */
    init {
        getFeaturedCategories()
        getTopLevelCategories()
    }

    private fun getFeaturedCategories() = viewModelScope.launch {
        safeGetFeaturedCategoriesCall()
    }

    private fun getTopLevelCategories() = viewModelScope.launch {
        safeGetTopLevelCategoriesCall()
    }
    private suspend fun safeGetFeaturedCategoriesCall(){
        featureCategories.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = appRepository.getFeatureCategories()
                featureCategories.postValue(handleFeatureCategoriesResponse(response))
            } else {
                featureCategories.postValue(Resource.Error("No internet connection"))
            }
        } catch (t : Throwable) {
            when (t){
                is IOException -> featureCategories.postValue(Resource.Error("Network Failure"))
                else -> featureCategories.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeGetTopLevelCategoriesCall(){
        topLevelCategories.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = appRepository.getTopLevelCategories()
                topLevelCategories.postValue(handleTopLevelCategoriesResponse(response))
            } else {
                topLevelCategories.postValue(Resource.Error("No internet connection"))
            }
        } catch (t : Throwable) {
            when (t){
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
        response: Response<List<TopLevelCategory>>
    ): Resource<List<TopLevelCategory>> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }
    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager
                .getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            /** TODO
             * remove use of deprecated without removing internet
             * checking for devices pre Marshmallow (API 23)
             */
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}