package com.sthoray.allright.ui.fragments.home.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.ui.fragments.home.adapter.HomeAdapter
import com.sthoray.allright.ui.main.view.MainActivity
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
     * Set up ViewModel, UI, and observers when the fragment view is created.
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
    }

    private fun setupRecyclerView() {
        mainAdapter = HomeAdapter()
        recViewFeaturedCategories.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun setOnClickListeners() {
        mainAdapter.setOnItemClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(MainActivity.CATEGORY_ID_KEY, it.id)
            this.startActivity(intent)
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
        progBarFeaturedCategories.visibility = View.VISIBLE
    }

    private fun removeProgressBar() {
        progBarFeaturedCategories.visibility = View.GONE
    }
}