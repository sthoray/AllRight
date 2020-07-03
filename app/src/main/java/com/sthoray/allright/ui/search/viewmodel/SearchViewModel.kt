package com.sthoray.allright.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sthoray.allright.data.model.SearchItem
import com.sthoray.allright.data.model.SearchRequest
import com.sthoray.allright.data.repository.MainRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.Dispatchers

/**
 * View model for bridging the View and Model components.
 *
 * @property mainRepository the data repository to interact with
 */
class SearchViewModel(private val mainRepository: MainRepository) : ViewModel() {

    /** The search request to post. */
    private var searchRequest = SearchRequest()

    /**
     * Search a AllGoods market place on a background thread using the IO Dispatcher.
     *
     * @return liveData containing the search response or null
     */
    fun search() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.search(searchRequest)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /**
     * Select a category leaving the rest of the search query alone.
     */
    fun setCategory(categoryId: Int) {
        searchRequest.categoryId = categoryId
        searchRequest.page = 1 // reset search to first page
    }
}