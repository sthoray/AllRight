package com.sthoray.allright.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel

/**
 * Factory class to define how the Main ViewModel should be created.
 */
class ViewModelProviderFactory(
    private val app: Application,
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
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app, appRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app, appRepository) as T
            }
            modelClass.isAssignableFrom(ListingViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ListingViewModel(app, appRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}