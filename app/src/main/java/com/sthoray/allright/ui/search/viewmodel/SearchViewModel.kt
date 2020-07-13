package com.sthoray.allright.ui.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    /** Search response. */
    val searchListings: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    /** Current search page number */
    private val searchListingsPage = 1

    /**
     * Search AllGoods for listings.
     */
    fun searchListings(searchQuery: String, categoryId: Int) = viewModelScope.launch {
        searchListings.postValue(Resource.Loading())
        val response = appRepository.searchListings(searchQuery, categoryId, searchListingsPage)
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
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }
}