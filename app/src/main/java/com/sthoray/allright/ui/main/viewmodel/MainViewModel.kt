package com.sthoray.allright.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sthoray.allright.data.repository.MainRepository
import com.sthoray.allright.utils.Resource
import kotlinx.coroutines.Dispatchers

/**
 * View model for bridging the View and Model components.
 *
 * Uses LiveData to expose observables when interacting with the Model. These
 * can be observed by the View.
 *
 * @property mainRepository the [MainRepository] to interact with
 */
class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getCategoryFeaturePanel() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getCategoryFeaturePanel()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}