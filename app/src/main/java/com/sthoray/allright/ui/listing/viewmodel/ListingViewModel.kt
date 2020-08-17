package com.sthoray.allright.ui.listing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.util.Internet
import com.sthoray.allright.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * View model for the listing activity
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 */
class ListingViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    /** The listing to display. */
    val listing: MutableLiveData<Resource<Listing>> = MutableLiveData()

    /**
     * Get all information about a listing.
     *
     * @param listingId the id of the listing to get
     */
    fun getListing(listingId: Int) = viewModelScope.launch {
        safeGetListing(listingId)
    }

    private suspend fun safeGetListing(listingId: Int) {
        listing.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.getListing(listingId)
                listing.postValue(handleListingResponse(response))
            } else {
                listing.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> listing.postValue(Resource.Error("Network Failure"))
                else -> listing.postValue(Resource.Error("Conversion Error"))
            }
        }
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