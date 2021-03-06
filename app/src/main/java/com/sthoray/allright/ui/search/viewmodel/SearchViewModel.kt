package com.sthoray.allright.ui.search.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sthoray.allright.R
import com.sthoray.allright.data.model.browse.BrowseResponse
import com.sthoray.allright.data.model.search.SearchRequest
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.utils.EspressoIdlingResource
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.SortOrder
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
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

    private val _searchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    private val _browseResponse: MutableLiveData<Resource<BrowseResponse>> = MutableLiveData()
    private val _draftSearchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    private val _draftBrowseResponse: MutableLiveData<Resource<BrowseResponse>> = MutableLiveData()

    /** The search request to search for. */
    lateinit var searchRequest: SearchRequest

    /**
     * The draft search request when selecting filters.
     *
     * The draft search request lets us work on a copy of the current [searchRequest].
     * This can be useful for setting filters as a user might want to discard all
     * their changes and return to the original request.
     */
    lateinit var searchRequestDraft: SearchRequest

    /** Last search response. */
    var lastSearchResponse: SearchResponse? = null

    /** Search response data containing results. */
    val searchResponse: LiveData<Resource<SearchResponse>> = _searchResponse

    /** Browse response data containing category information. */
    val browseResponse: LiveData<Resource<BrowseResponse>> = _browseResponse

    /**
     * Draft search response data containing results.
     *
     * This is used for checking the result of a potential [searchRequestDraft]. Allows
     * us to navigate through subcategories without discarding the actual [searchResponse].
     */
    val draftSearchResponse: LiveData<Resource<SearchResponse>> = _draftSearchResponse

    /**
     * Draft browse response data containing category info.
     *
     * This is used for checking the result of a potential [searchRequestDraft]. Allows
     * us to navigate through subcategories without discarding the actual [searchResponse].
     */
    val draftBrowseResponse: LiveData<Resource<BrowseResponse>> = _draftBrowseResponse

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
                SearchRequest(categoryId = categoryId).setMarketplace(mall = false)
            } else {
                SearchRequest(categoryId = categoryId).setMarketplace(mall = true)
            }
            searchRequestDraft = searchRequest
            search()
        }
    }

    /**
     * Search AllGoods for listings.
     */
    fun search() = viewModelScope.launch {
        launch { safeSearchCall() }
        launch { safeBrowseCall() }
    }

    /**
     * Search AllGoods for listings using the draft request.
     *
     * This will modify draft resources instead of the actual search resources.
     */
    fun draftSearch() = viewModelScope.launch {
        searchRequestDraft.pageNumber = 1
        launch { safeDraftSearchCall() }
        launch { safeDraftBrowseCall() }
    }


    // ==========================================
    // Search API calls
    // ==========================================

    private suspend fun safeSearchCall() {
        _searchResponse.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.searchListings(searchRequest)
                _searchResponse.postValue(handleSearchResponse(response))
            } else {
                _searchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }

        } catch (e: Exception) {
            e.message?.let { Timber.e(it) }

            when (e) {
                is IOException -> _searchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> _searchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private fun handleSearchResponse(
        response: Response<SearchResponse>
    ): Resource<SearchResponse> {

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                searchRequest.pageNumber++
                if (lastSearchResponse == null) {
                    // First page: replace existing list
                    lastSearchResponse = responseBody

                } else {
                    // New page: add to existing list
                    val oldListings = lastSearchResponse?.data
                    val newListings = responseBody.data
                    oldListings?.addAll(newListings)

                }
                //Decrement the idling resource for testing
                EspressoIdlingResource.decrement()

                return Resource.Success(lastSearchResponse ?: responseBody)

            }
        }
        return Resource.Error(response.message())
    }


    // ==========================================
    // Browse API calls
    // ==========================================

    private suspend fun safeBrowseCall() {

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        _browseResponse.postValue(Resource.Loading())

        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.browseCategory(
                    categoryId = searchRequest.categoryId,
                    type = searchRequest.type()
                )
                _browseResponse.postValue(processBrowseResponse(response))
            } else {
                _browseResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_network_error)))
            }

            //Decrement the idling resource for testing
            EspressoIdlingResource.decrement()

        } catch (e: Exception) {
            e.message?.let { Timber.e(it) }

            when (e) {
                is IOException -> _browseResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.api_error_network
                        )
                    )
                )
                else -> _browseResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.api_error_conversion
                        )
                    )
                )
            }
        }
    }

    private fun processBrowseResponse(
        response: Response<BrowseResponse>
    ): Resource<BrowseResponse> {

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        if (response.isSuccessful) {
            response.body()?.let {

                //Decrement the idling resource for testing
                EspressoIdlingResource.decrement()

                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    // ==========================================
    // Draft search API calls
    // ==========================================

    private suspend fun safeDraftSearchCall() {
        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        _draftSearchResponse.postValue(Resource.Loading())
        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.searchListings(searchRequestDraft)
                //Decrement the idling resource for testing
                EspressoIdlingResource.decrement()

                _draftSearchResponse.postValue(handleDraftSearchResponse(response))
            } else {
                _draftSearchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_network_error)
                    )
                )
            }

        } catch (e: Exception) {
            e.message?.let { Timber.e(it) }

            when (e) {
                is IOException -> _draftSearchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_network)
                    )
                )
                else -> _draftSearchResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.api_error_conversion)
                    )
                )
            }
        }
    }

    private fun handleDraftSearchResponse(
        response: Response<SearchResponse>
    ): Resource<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeDraftBrowseCall() {
        _draftBrowseResponse.postValue(Resource.Loading())

        try {
            if (Internet.hasConnection(getApplication())) {
                val response = appRepository.browseCategory(
                    categoryId = searchRequestDraft.categoryId,
                    type = searchRequestDraft.type()
                )
                _draftBrowseResponse.postValue(processBrowseResponse(response))
            } else {
                _draftBrowseResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.no_network_error
                        )
                    )
                )
            }

        } catch (e: Exception) {
            e.message?.let { Timber.e(it) }

            when (e) {
                is IOException -> _draftBrowseResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.api_error_network
                        )
                    )
                )
                else -> _draftBrowseResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.api_error_conversion
                        )
                    )
                )
            }
        }
    }


    // ==========================================
    // More actions
    // ==========================================


    /**
     * Clear the search result and begin searching again for fresh data.
     */
    fun refreshSearchResults() {
        _searchResponse.postValue(Resource.Success(data = null))
        searchRequest.pageNumber = 1
        lastSearchResponse = null
        search()
    }

    /**
     * Setup the draft search request.
     */
    fun initFilters() {
        searchRequestDraft = searchRequest.copy()
    }

    /** Make the draft search request active, clear the last search, then begin searching. */
    fun applyFiltersAndSearch() {
        searchRequest = searchRequestDraft.copy()
        searchRequest.pageNumber = 1
        lastSearchResponse = null
        search()
    }

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
        searchRequestDraft = searchRequestDraft.setMarketplace(isMall)
    }


    // ==========================================
    // Helper functions
    // ==========================================


    /**
     * Check what marketplace a [searchRequest] will search.
     *
     * @return True if the marketplace is "mall" and "false" if it is "secondhand".
     */
    fun SearchRequest.isMall() = (this.auctions == 0) && (this.products == 1)

    private fun SearchRequest.type() = (this.isMall().toInt() - 2) * -1 // 1 = mall, 2 = secondhand

    private fun SearchRequest.setMarketplace(mall: Boolean): SearchRequest {
        return this.apply {
            auctions = (!mall).toInt()
            products = mall.toInt()
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0


    companion object {
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
}
