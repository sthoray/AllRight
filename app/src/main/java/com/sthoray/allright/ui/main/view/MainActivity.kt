package com.sthoray.allright.ui.main.view

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
import com.sthoray.allright.data.api.RetrofitBuilder
import com.sthoray.allright.data.model.FeatureCategory
import com.sthoray.allright.ui.base.ViewModelFactory
import com.sthoray.allright.ui.main.adapter.MainAdapter
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The main activity where everything starts.
 *
 * This is the "home page" where the user us presented with suggestions to
 * start searching and can view their past search queries.
 */
class MainActivity : AppCompatActivity() {

    /** The ViewModel that this View subscribes to. */
    private lateinit var viewModel: MainViewModel

    /** The adapter for updating views. */
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        ).get(MainViewModel::class.java)
    }

    /**
     * Setup the UI to its initial state.
     */
    private fun setupUI() {
        adapter = MainAdapter(arrayListOf()) // set to empty ArrayList
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                recyclerView.context,
//                (recyclerView.layoutManager as LinearLayoutManager).orientation
//            )
//        )
        recyclerView.adapter = adapter
    }

    /**
     * Define View behaviour based on the [Status] of the fetched data.
     */
    private fun setupObservers() {
        viewModel.getCategoryFeaturePanel().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { featuredCategories ->
                            retrieveList(featuredCategories.categories.values.toList())
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

    private fun retrieveList(featuredCategories: List<FeatureCategory>) {
        adapter.apply {
            addFeaturedCategories(featuredCategories)
            notifyDataSetChanged()
        }
    }
}