package com.sthoray.allright.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.data.api.ApiHelper
import com.sthoray.allright.data.repository.MainRepository
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import java.lang.IllegalArgumentException

/**
 * Factory class for providing a View Model.
 *
 * @property apiHelper the [ApiHelper] to interact with
 */
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    /**
     * Create a new instance of the given class.
     *
     * @param modelClass a Class whose instance is requested
     * @param T the type parameter for the ViewModel.
     *
     * @return T a newly created ViewModel
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}