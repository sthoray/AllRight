package com.sthoray.allright.ui.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * View model for the Search activity.
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property appRepository the data repository to interact with
 */
class SearchViewModel(
    private val appRepository: AppRepository
) : ViewModel() {


    /** The search request to perform. */
    var searchRequest = SearchRequest()

    /** Search listings data. */
    val searchListings: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    /** Last search listings response. */
    var searchListingsResponse: SearchResponse? = null


    /** Search AllGoods for listings. */
    fun searchListings() = viewModelScope.launch {
        searchListings.postValue(Resource.Loading())
        val response = appRepository.searchListings(searchRequest)
        searchListings.postValue(handleSearchListingsResponse(response))
    }

    /**
     * Emit successful or failed responses.
     *
     * @param response the network response to handle
     *
     * @return a [Resource] containing data or an error message
     */
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
}