package com.sthoray.allright.ui.search.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.main.view.MainActivity
import com.sthoray.allright.ui.search.adapter.ResultsAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Constants.Companion.BASE_PRODUCT_URL
import com.sthoray.allright.utils.Resource

/**
 * Activity for viewing search results.
 *
 * When the search activity is launched it will display the results of the
 * last search query triggered by the parent activity. The search query can
 * be modified to return new results.
 */
class SearchActivity : AppCompatActivity() {


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
        setContentView(R.layout.activity_search)
        setupViewModel()


        viewModel.searchRequest.categoryId = intent.getIntExtra(
            MainActivity.CATEGORY_ID_KEY,
            0   // search all categories
        )
        viewModel.searchListings() // Bad idea (try rotating the device)
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(SearchViewModel::class.java)
    }
}