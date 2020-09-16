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
import com.sthoray.allright.utils.SortOrder
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

    /** The search request to search for. */
    lateinit var searchRequest: SearchRequest

    /** Search listings data. */
    val searchListings: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    /** Last search listings response. */
    var searchListingsResponse: SearchResponse? = null

    /**
     * Initialise [searchRequest] and perform the first search.
     *
     * If [searchRequest] needs to be initialised (e.g. when the activity
     * has been created from an intent for the first time) then the first
     * search must also be performed to fetch the first page.
     *
     * @param categoryId The ID of the category to begin the search in.
     * @param marketplace The marketplace to start searching in. Accepts
     * either "mall" or "secondhand".
     */
    fun initSearch(categoryId: Int, marketplace: String) {
        if (!::searchRequest.isInitialized) {
            searchRequest = if (marketplace == "secondhand") {
                setMarketplace(SearchRequest(categoryId = categoryId), false)
            } else {
                setMarketplace(SearchRequest(categoryId = categoryId), true)
            }
            searchRequestDraft = searchRequest
            searchListings()
        }
    }

    /** Search AllGoods for listings. */
    fun searchListings() = viewModelScope.launch {
        safeSearchCall()
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

    private fun handleSearchListingsResponse(
        response: Response<SearchResponse>
    ): Resource<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                searchRequest.pageNumber++
                if (searchListingsResponse == null) {
                    searchListingsResponse = responseBody
                } else {
                    val oldListings = searchListingsResponse!!.data
                    val newListings = responseBody.data
                    oldListings.addAll(newListings)
                }
                return Resource.Success(searchListingsResponse ?: responseBody)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * The draft search request when selecting filters.
     *
     * The draft search request lets us work on a copy of the current [searchRequest].
     * This can be useful for setting filters as a user might want to discard all
     * their changes and return to the original request.
     */
    lateinit var searchRequestDraft: SearchRequest

    /** Make the draft search request active, clear the last search, then begin searching. */
    fun applyFiltersAndSearch() {
        searchRequest = searchRequestDraft.copy()
        searchRequest.pageNumber = 1
        searchListingsResponse = null
        searchListings()
    }

    /** Clear the search result and begin searching again for fresh data. */
    fun refreshSearchResults() {
        searchListings.postValue(Resource.Success(data = null))
        searchListingsResponse = null
        searchRequest.pageNumber = 1
        searchListingsResponse = null
        searchListings()
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    /**
     * Set [searchRequestDraft]s binary filters.
     *
     * @param freeShipping True if listings should have free shipping, else false.
     * @param fastShipping True if products should have fast shipping, else false.
     * @param brandNew True if products should be brand new, else false.
     */
    fun setDraftBinaryFilters(freeShipping: Boolean, fastShipping: Boolean, brandNew: Boolean) {
        searchRequestDraft.freeShipping = freeShipping.toInt()
        searchRequestDraft.fastShipping = fastShipping.toInt()
        searchRequestDraft.brandNew = brandNew.toInt()
    }

    /**
     * Set [searchRequestDraft]s marketplace.
     *
     * This currently may set incorrect options depending on the search
     * category. For example, if we are searching for vehicles different
     * rules apply.
     *
     * @param isMall True if the marketplace is "mall", false if it is "secondhand".
     */
    fun setDraftMarketplace(isMall: Boolean) {
        searchRequestDraft = setMarketplace(searchRequestDraft, isMall)
    }

    private fun setMarketplace(searchRequest: SearchRequest, isMall: Boolean): SearchRequest {
        return searchRequest.apply {
            auctions = (!isMall).toInt()
            products = isMall.toInt()
        }
    }

    /**
     * Check what market place the [searchRequest] will search.
     *
     * @param searchRequest The [SearchRequest] to check.
     *
     * @return True if the marketplace is "mall" and "false" if it is "secondhand".
     */
    fun isMall(searchRequest: SearchRequest): Boolean {
        return (searchRequest.auctions == 0) && (searchRequest.products == 1)
    }

    /** List of [SortOrder] option for the AllGoods mall marketplace. */
    val sortOrdersMall = listOf(
        SortOrder.BEST,
        SortOrder.POPULAR,
        SortOrder.PRICE_SHIPPED_LOWEST,
        SortOrder.PRICE_LOWEST,
        SortOrder.PRICE_HIGHEST,
        SortOrder.NEW_PRODUCTS,
        SortOrder.SALE,
        SortOrder.ALPHABETICAL
    )

    /** List of [SortOrder] option for the AllGoods secondhand marketplace. */
    val sortOrdersSecondhand = listOf(
        SortOrder.BEST,
        SortOrder.TRENDING,
        SortOrder.NEW_LISTINGS,
        SortOrder.CLOSING_SOON,
        SortOrder.MOST_BIDS,
        SortOrder.BUY_NOW_LOWEST,
        SortOrder.BUY_NOW_HIGHEST,
        SortOrder.PRICE_LOWEST,
        SortOrder.PRICE_HIGHEST,
        SortOrder.ALPHABETICAL
    )
}