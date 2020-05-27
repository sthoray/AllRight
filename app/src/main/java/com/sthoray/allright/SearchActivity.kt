package com.sthoray.allright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_search)

        recyclerView_searchResults.layoutManager = LinearLayoutManager(this)
        recyclerView_searchResults.adapter = SearchResultAdapter()

        // Update nav bar title
        val navBarTitle = intent.getStringExtra(FeaturedCategoryViewHolder.SEARCH_KEY)
        supportActionBar?.title = navBarTitle
    }

    /**
     * Adapter for search results.
     *
     * Populates the search results recycler view by creating search result view holders
     * as required.
     */
    private class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

        override fun getItemCount(): Int {
            return 20
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val searchResultView = layoutInflater.inflate(R.layout.search_result_row,
                parent,
                false)
            return SearchResultViewHolder(searchResultView)
        }

        override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {

        }

    }

    /**
     * View holder for search results.
     *
     * Responsible for displaying a single search result and providing an  on click listener.
     */
    private class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

}
