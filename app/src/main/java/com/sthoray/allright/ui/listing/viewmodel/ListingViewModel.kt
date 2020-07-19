package com.sthoray.allright.ui.listing.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * View model for the listing activity
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property appRepository the data repository to interact with
 */
class ListingViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    /** The listing to display. */
    val listing: MutableLiveData<Resource<Listing>> = MutableLiveData()

    /** The actual listing response from the AllGoods API. */
    val listingResponse: Listing? = null

    /**
     * Get all information about a listing.
     *
     * @param listingId the id of the listing to get
     */
    fun getListing(listingId: Int) = viewModelScope.launch {
        listing.postValue(Resource.Loading())
        val response = appRepository.getListing(listingId)
        listing.postValue(handleListingResponse(response))
    }

    private fun handleListingResponse(
        response: Response<Listing>
    ): Resource<Listing> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                return Resource.Success(responseBody)
            }
        }
        return Resource.Error(response.message())
    }
}