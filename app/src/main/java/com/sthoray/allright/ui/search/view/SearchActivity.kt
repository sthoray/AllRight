package com.sthoray.allright.ui.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.search.adapter.SearchAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.ui.search.viewmodel.SearchViewModelProviderFactory
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Activity for viewing search results.
 *
 * When the search activity is launched it will display the results of the
 * last search query triggered by the parent activity. The search query can
 * be modified to return new results.
 */
class SearchActivity : AppCompatActivity() {


    /** The ViewModel to interact with data. */
    private lateinit var viewModel: SearchViewModel

    /** The adapter for displaying retrieved data. */
    private lateinit var searchAdapter: SearchAdapter

    private val TAG = "SearchActivity"

    /**
     * Set up ViewModel, UI, and observers when the activity is created.
     *
     * @param savedInstanceState and data saved if the activity is being re-initialized
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupViewModel()
        setupUI()
        setupObservers()
    }


    /** Initialise the View Model for this activity. */
    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(this))
        val viewModelProviderFactory = SearchViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(SearchViewModel::class.java)
    }

    /** Setup the UI. */
    private fun setupUI() {
        searchAdapter = SearchAdapter()
        recViewSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    /** Subscribe to observable data and define View behaviour. */
    private fun setupObservers() {
        viewModel.searchListings.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    removeProgressBar()
                    response.data?.let { listingResponse ->
                        val listings = listingResponse.data
                        searchAdapter.differ.submitList(listings)
                    }
                }
                is Resource.Error -> {
                    removeProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        progBarSearchPagination.visibility = View.VISIBLE
    }

    private fun removeProgressBar() {
        progBarSearchPagination.visibility = View.GONE
    }


    /**
     * Search with a given query.
     *
     * @param categoryId the category's id to search in
     */
    private fun newSearch(categoryId: Int) {
        MainScope().launch {
            viewModel.searchListings("", categoryId)
        }
    }
}