package com.sthoray.allright.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
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

    /**
     * Search a AllGoods market place on a background thread using the IO Dispatcher.
     *
     * @return liveData with a [SearchResponse] or null as data
     */
    fun search(searchRequest: SearchRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.search(searchRequest)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}