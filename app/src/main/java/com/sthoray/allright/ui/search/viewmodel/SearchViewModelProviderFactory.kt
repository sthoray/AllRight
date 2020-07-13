package com.sthoray.allright.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.data.repository.AppRepository

/**
 * Factory class to define how the Search ViewModel should be created.
 *
 * @property appRepository the listing repository
 */
class SearchViewModelProviderFactory(
    private val appRepository: AppRepository
) : ViewModelProvider.Factory {

    /**
     * Create a new instance of the provided class.
     *
     * @param modelClass a class whose instance is requested
     * @param T the type parameter for the ViewModel.
     *
     * @return T a newly created ViewModel
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(appRepository) as T
    }
}