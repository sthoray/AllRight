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
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.view.ListingActivity
import com.sthoray.allright.ui.main.view.MainActivity
import com.sthoray.allright.ui.search.adapter.SearchAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Constants.Companion.BASE_PRODUCT_URL
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Activity for viewing search results.
 *
 * When the search activity is launched it will display the results of the
 * last search query triggered by the parent activity. The search query can
 * be modified to return new results.
 */
class SearchActivity : AppCompatActivity() {


    private lateinit var viewModel: SearchViewModel
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

        viewModel.searchRequest.categoryId = intent.getIntExtra(
            MainActivity.CATEGORY_ID_KEY,
            0   // search all categories
        )
        viewModel.searchListings() // Bad idea (try rotating the device)
    }


    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(SearchViewModel::class.java)
    }

    // Booleans to help with pagination
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible =
                totalItemCount >= viewModel.searchListingsResponse!!.meta.pagination.perPage
            val shouldPaginate = isNotLoadingNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchListings()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupUI() {
        searchAdapter = SearchAdapter()
        recViewSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@SearchActivity.scrollListener)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        searchAdapter.setOnItemClickListener {
            /*
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BASE_PRODUCT_URL + it.id)
            this.startActivity(intent)
            */


            val intent = Intent(this, ListingActivity::class.java)
            this.startActivity(intent)


        }
    }

    private fun setupObservers() {
        viewModel.searchListings.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { listingResponse ->
                        // Not sure why differ is not working when providing a MutableList
                        searchAdapter.differ.submitList(listingResponse.data.toList())
                        isLastPage =
                            viewModel.searchRequest.pageNumber == listingResponse.meta.pagination.totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
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
        isLoading = true
    }

    private fun hideProgressBar() {
        progBarSearchPagination.visibility = View.GONE
        isLoading = false
    }
}