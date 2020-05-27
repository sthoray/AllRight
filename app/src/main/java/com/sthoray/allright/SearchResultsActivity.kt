package com.sthoray.allright

import SearchResultsAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class SearchResultsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        recyclerView_topLevel.layoutManager = LinearLayoutManager(this)
        recyclerView_topLevel.adapter = SearchResultsAdapter()
    }

    class SearchResultsViewHolder(val searchResultsView : View) : RecyclerView.ViewHolder(searchResultsView)
}
