package com.sthoray.allright.ui.search.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.main.view.MainActivity.Companion.CATEGORY_ID_KEY
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.EspressoIdlingResource


/**
 * Activity for viewing search results.
 *
 * When the search activity is launched it will display the results of the
 * last search query triggered by the parent activity. The search query can
 * be modified to return new results.
 */
class SearchActivity : AppCompatActivity() {

    companion object {

        /** The key for a selected listingId. */
        const val LISTING_ID_KEY = "LISTING_ID"
    }

    /**
     * Search activity's share view model.
     *
     * By creating an activity level view model and accessing it in
     * each main fragment, we can reduce the number of API calls.
     */
    lateinit var viewModel: SearchViewModel

    /**
     * Set up ViewModel.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        setContentView(R.layout.activity_search)
        setupViewModel()
        setupSearchRequest(intent.getIntExtra(CATEGORY_ID_KEY, 0))

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()

    }

    private fun setupViewModel() {
        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(SearchViewModel::class.java)

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()
    }

    private fun setupSearchRequest(categoryId: Int) {
        val marketplace = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString("default_marketplace", "mall")!!
        viewModel.initSearch(categoryId, marketplace)
    }
}