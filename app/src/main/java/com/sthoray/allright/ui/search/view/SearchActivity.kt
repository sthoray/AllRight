package com.sthoray.allright.ui.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sthoray.allright.R
import com.sthoray.allright.ui.search.adapter.SearchAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}