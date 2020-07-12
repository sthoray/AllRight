package com.sthoray.allright.ui.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.api.ApiHelper
import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.model.search.Listing
import com.sthoray.allright.data.model.search.SearchResponseMetadata
import com.sthoray.allright.ui.base.ViewModelFactory
import com.sthoray.allright.ui.main.adapter.MainAdapter
import com.sthoray.allright.ui.search.adapter.SearchAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Status
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Activity for viewing search results.
 *
 * When the search activity is launched it will display the results of the
 * last search query triggered by the parent activity. The search query can
 * be modified to return new results.
 */
class SearchActivity : AppCompatActivity() {

    /** The ViewModel that this View subscribes to. */
    private lateinit var viewModel: SearchViewModel

    /** The adapter for updating views. */
    private lateinit var adapter: SearchAdapter

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
        setupSearchRequest()
        setupObservers()
    }

    /**
     * Initialise the View Model for this activity.
     */
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitInstance.apiService))
        ).get(SearchViewModel::class.java)
    }

    /**
     * Modify the search request to match the intent.
     */
    private fun setupSearchRequest() {
        viewModel.setCategory(
            intent.getIntExtra(
                MainAdapter.FeatureCategoryViewHolder.CATEGORY_ID_KEY,
                0
            )
        )
    }

    /**
     * Setup the UI to its initial state.
     */
    private fun setupUI() {
        adapter = SearchAdapter(arrayListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    /**
     * Define View behaviour based on the [Status] of the fetched data.
     */
    private fun setupObservers() {
        viewModel.search().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { searchResponse ->
                            retrieveList(
                                searchResponse.data,
                                searchResponse.meta
                            )
                        }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(searchItems: List<Listing>, searchMeta: SearchResponseMetadata) {
        adapter.apply {
            addItems(searchItems, searchMeta)
            notifyDataSetChanged()
        }
    }
}