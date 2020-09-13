package com.sthoray.allright.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.ui.main.adapter.HomeAdapter
import com.sthoray.allright.ui.main.view.MainActivity.Companion.CATEGORY_ID_KEY
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.search.view.SearchActivity
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Fragment for the home page.
 *
 * Contains featured categories. More content will be added to this
 * fragment (e.g. recent searches) in the future.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: HomeAdapter
    private val TAG = "HomeFragment"

    /**
     * Set up ViewModel, UI, and observers.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        setOnClickListeners()
        setupObservers()
        swipeToRefreshHomeFragment()
    }

    private fun swipeToRefreshHomeFragment(){
        swipeRefresh.setOnRefreshListener {
            Toast.makeText(context, "Page refreshed!", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        mainAdapter = HomeAdapter()
        recyclerViewFeaturedCategories.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(activity, 3)
        }
    }

    private fun setOnClickListeners() {
        mainAdapter.setOnItemClickListener { category ->
            Intent(activity, SearchActivity::class.java).also {
                it.putExtra(CATEGORY_ID_KEY, category.id)
                startActivity(it)
            }
        }
    }

    private fun setupObservers() {
        viewModel.featureCategories.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    removeProgressBar()
                    response.data?.let { featureCategoriesResponse ->
                        val categories = featureCategoriesResponse.categories.values.toList()
                        mainAdapter.differ.submitList(categories)
                    }
                }
                is Resource.Error -> {
                    removeProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
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
        progressBarFeaturedCategories.visibility = View.VISIBLE
    }

    private fun removeProgressBar() {
        progressBarFeaturedCategories.visibility = View.GONE
    }
}