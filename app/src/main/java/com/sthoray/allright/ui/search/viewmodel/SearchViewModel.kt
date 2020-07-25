package com.sthoray.allright.ui.search.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * View model for the Search activity.
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property appRepository the data repository to interact with
 */
class SearchViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {


    /** The search request to perform. */
    var searchRequest = SearchRequest()

    /** Search listings data. */
    val searchListings: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    /** Last search listings response. */
    var searchListingsResponse: SearchResponse? = null


    /** Search AllGoods for listings. */
    fun searchListings() = viewModelScope.launch {
        safeSearchCall()
    }

    private fun handleSearchListingsResponse(
        response: Response<SearchResponse>
    ): Resource<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                searchRequest.pageNumber++
                if (searchListingsResponse == null) {
                    searchListingsResponse = responseBody
                } else {
                    val oldListings = searchListingsResponse?.data
                    val newListings = responseBody.data
                    oldListings?.addAll(newListings)
                }
                return Resource.Success(searchListingsResponse ?: responseBody)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchCall() {
        searchListings.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.searchListings(searchRequest)
                searchListings.postValue(handleSearchListingsResponse(response))
            } else {
                searchListings.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchListings.postValue(Resource.Error("Network Failure"))
                else -> searchListings.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

}