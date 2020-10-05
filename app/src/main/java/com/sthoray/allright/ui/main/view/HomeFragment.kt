package com.sthoray.allright.ui.main.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import com.sthoray.allright.utils.Constants.Companion.BASE_URL
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
    private val DEBUG_TAG = "HomeFragment"

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
        setupView()
        setupObservers()
    }

    private fun setupView() {
        // Recycler view
        mainAdapter = HomeAdapter()
        rvFeaturedCategories.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(activity, 3)
        }

        // Swipe to refresh listener
        srlHome.setOnRefreshListener {
            viewModel.refreshHomeFragment()
        }

        // Featured panel category selected
        mainAdapter.setOnItemClickListener { category ->
            val vehiclesCategoryId = 1
            if (category.id == vehiclesCategoryId) {
                AlertDialog.Builder(activity)
                    .setMessage(R.string.marketplace_not_supported_message)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        Intent(Intent.ACTION_VIEW).also {
                            it.data = Uri.parse(BASE_URL + "vehicles")
                            this.startActivity(it)
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            } else if (category.isRestricted == 1) {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.restricted_category_title)
                    .setMessage(R.string.restricted_category_message)
                    .setPositiveButton(R.string.restricted_category_continue, DialogInterface.OnClickListener { _, _ ->
                        Intent(activity, SearchActivity::class.java).also {
                            it.putExtra(CATEGORY_ID_KEY, category.id)
                            startActivity(it)
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            } else {
                Intent(activity, SearchActivity::class.java).also {
                    it.putExtra(CATEGORY_ID_KEY, category.id)
                    startActivity(it)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.featureCategories.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { featureCategoriesResponse ->
                        val categories = featureCategoriesResponse.categories.values.toList()
                        mainAdapter.differ.submitList(categories)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        Log.e(DEBUG_TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        srlHome.isRefreshing = true
    }

    private fun hideProgressBar() {
        srlHome.isRefreshing = false
    }
}