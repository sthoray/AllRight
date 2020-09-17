package com.sthoray.allright.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.listing.viewmodel.ListingImagesViewModel
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel

/**
 * ViewModel provider factory to instantiate ViewModels.
 * Required for ViewModels which have a non-empty constructor.
 */
class ViewModelProviderFactory(
    private val app: Application,
    private val appRepository: AppRepository
) : ViewModelProvider.Factory {

    /**
     * Create a new instance of the provided class.
     *
     * @param modelClass The ViewModel class whose instance is requested.
     * @param T The type parameter for the ViewModel.
     *
     * @return T a newly created ViewModel.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(app, appRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                return SearchViewModel(app, appRepository) as T
            }
            modelClass.isAssignableFrom(ListingViewModel::class.java) -> {
                return ListingViewModel(app, appRepository) as T
            }
            modelClass.isAssignableFrom(ListingImagesViewModel::class.java) -> {
                return ListingImagesViewModel(app, appRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}