package com.sthoray.allright.ui.listing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.R
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * View model for the listing activity
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 */
class ListingImagesViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    /** The listing to display. */
    val listingImages: MutableLiveData<Resource<List<Image>>> = MutableLiveData()




}