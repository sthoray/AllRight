package com.sthoray.allright.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sthoray.allright.data.model.CategoryFeaturePanelResponse
import com.sthoray.allright.data.repository.MainRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.Dispatchers

/**
 * View model for bridging the View and Model components.
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property mainRepository the data repository to interact with
 */
class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    /**
     * Get featured categories on a background thread using the IO Dispatcher.
     *
     * If the the HTTP request was performed successfully, data will contain
     * a [CategoryFeaturePanelResponse]. If an exception is thrown when performing the
     * request, it is handled here.
     *
     * @return liveData with a list of feature categories or null as data
     */
    fun getFeaturedCategories() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getCategoryFeaturePanel().categories.values.toList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}