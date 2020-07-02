package com.sthoray.allright.ui.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.api.ApiHelper
import com.sthoray.allright.data.api.RetrofitBuilder
import com.sthoray.allright.ui.base.ViewModelFactory
import com.sthoray.allright.ui.search.adapter.SearchAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerView
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
        setupObservers()
    }

    /**
     * Initialise the View Model for this activity.
     */
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(SearchViewModel::class.java)
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

    }
}