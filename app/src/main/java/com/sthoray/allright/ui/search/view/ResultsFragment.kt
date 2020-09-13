package com.sthoray.allright.ui.search.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sthoray.allright.R
import com.sthoray.allright.ui.listing.view.ListingActivity
import com.sthoray.allright.ui.search.adapter.ResultsAdapter
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_results.*

/** Fragment to display search results in a recycler view. */
class ResultsFragment : Fragment(R.layout.fragment_results) {


    private lateinit var viewModel: SearchViewModel
    private lateinit var resultsAdapter: ResultsAdapter
    private val TAG = "ResultsFragment"


    /**
     * Set up ViewModel, UI, and observers.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as SearchActivity).viewModel
        setupRecyclerView()
        setupFab()
        setListingOnClickListeners()
        setupObservers()
        swipeToRefreshResultFragment()
    }

    private fun swipeToRefreshResultFragment() {
        swipeRefreshResult.setOnRefreshListener {
            viewModel.searchListings
            Toast.makeText(context, "Page refreshed!", Toast.LENGTH_SHORT).show()
            swipeRefreshResult.isRefreshing = false
        }
    }


    private fun setupRecyclerView() {
        resultsAdapter = ResultsAdapter()
        rvResultsListings.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@ResultsFragment.scrollListener)
        }
    }

    private fun setupFab() {
        extendedFabFilter.setOnClickListener {
            viewModel.searchRequestDraft = viewModel.searchRequest.copy()
            findNavController().navigate(R.id.action_navigation_results_to_navigation_filters)
        }
    }

    private fun setListingOnClickListeners() {
        resultsAdapter.setOnItemClickListener { listing ->
            Intent(activity, ListingActivity::class.java).also {
                it.putExtra(LISTING_ID_KEY, listing.id)
                startActivity(it)
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchListings.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { listingResponse ->
                        // Not sure why differ is not working when providing a MutableList
                        resultsAdapter.differ.submitList(listingResponse.data.toList())
                        isLastPage =
                            viewModel.searchRequest.pageNumber == listingResponse.meta.pagination.totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                        Toast.makeText(
                            activity, "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    // Pagination
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

            val sensitivity = 13 // This may not be right yet
            if (dy < -sensitivity) {
                extendedFabFilter.extend()
            } else if (dy > sensitivity) {
                extendedFabFilter.shrink()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun showProgressBar() {
        if (resultsAdapter.itemCount == 0) {
            pbResultsListingsLoading.visibility = View.VISIBLE
            pbResultsListingsPagination.visibility = View.GONE
        } else {
            pbResultsListingsLoading.visibility = View.GONE
            pbResultsListingsPagination.visibility = View.VISIBLE
        }
        isLoading = true
    }

    private fun hideProgressBar() {
        pbResultsListingsLoading.visibility = View.GONE
        pbResultsListingsPagination.visibility = View.GONE
        isLoading = false
    }
}