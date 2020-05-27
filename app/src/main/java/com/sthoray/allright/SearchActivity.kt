package com.sthoray.allright

import SearchResultsAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_search)

        recyclerView_searchResults.layoutManager = LinearLayoutManager(this)
        recyclerView_searchResults.adapter = SearchResultsAdapter()
    }

    class SearchResultsViewHolder(val searchResultsView : View) : RecyclerView.ViewHolder(searchResultsView)
}
