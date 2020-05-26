package com.sthoray.allright

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
    private class SearchResultsAdapter: RecyclerView.Adapter<SearchResultsViewHolder>(){
        override fun getItemCount(): Int {
            return 5
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
            val blueView = View(parent.context)
            blueView.setBackgroundColor(Color.BLUE)
            blueView.minimumHeight = 50
            return SearchResultsViewHolder(blueView)
        }

        override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {

        }
    }
    private class SearchResultsViewHolder(val searchResultsView : View) : RecyclerView.ViewHolder(searchResultsView){

    }
}