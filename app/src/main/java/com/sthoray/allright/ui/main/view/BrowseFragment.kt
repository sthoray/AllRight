package com.sthoray.allright.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.ui.main.adapter.BrowseAdapter
import com.sthoray.allright.ui.main.view.MainActivity.Companion.CATEGORY_ID_KEY
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.search.view.SearchActivity
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_browse.*

/**
 * Fragment for exploring AllGoods through the top level categories.
 *
 * Displays a list of top level categories. Clicking on one of the
 * categories will launch the search activity to refine the search
 * query further.
 */
class BrowseFragment : Fragment(R.layout.fragment_browse) {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: BrowseAdapter
    private val TAG = "BrowseFragment"

    /**
     * Setup the ViewModel, UI and observers.
     *
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        setOnClickListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        mainAdapter = BrowseAdapter()
        recyclerViewTopLevelCategories.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
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
        viewModel.topLevelCategories.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { topLevelCategories ->
                        mainAdapter.differ.submitList(topLevelCategories)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
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
        progressBarTopLevelCategories.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarTopLevelCategories.visibility = View.GONE
    }
}