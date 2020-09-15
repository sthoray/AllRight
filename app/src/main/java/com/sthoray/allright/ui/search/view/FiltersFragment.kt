package com.sthoray.allright.ui.search.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.ui.search.adapter.CategoryAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_filters.*

/**
 * Search filtering fragment.
 *
 * This fragment provides a central place to view, modify, and remove all
 * filters for the current search request.
 */
class FiltersFragment : Fragment(R.layout.fragment_filters) {


    private lateinit var viewModel: SearchViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private var DEBUG_TAG = "FiltersFragment"


    /**
     * Setup the filters fragment.
     *
     * Shows all available filters for the current marketplace. The current
     * search parameters are set as the starting point.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as SearchActivity).viewModel
        setupCategoryRecyclerView()
        updateFilters()
        setListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.draftSearchListings.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.meta?.categories?.let {
                        categoryAdapter.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.e(DEBUG_TAG, "An error occurred: $it")
                        Toast.makeText(
                            activity, "An error occurred: $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter()
        rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context)
            //addOnScrollListener(this@FiltersFragment.)
        }
    }

    private fun updateFilters() {
        if (viewModel.isMall(viewModel.searchRequestDraft)) {
            showMallFilters()
            setMallFilters()
        } else {
            showSecondhandFilters()
            setSecondhandFilters()
        }
    }

    private fun showMallFilters() {
        val sortOrderTitleMall = ArrayList<String>()
        viewModel.sortOrdersMall.forEach { sortOrderTitleMall.add(getString(it.resourceId)) }

        spSortBy.adapter = ArrayAdapter<String>(
            this@FiltersFragment.requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            sortOrderTitleMall
        )

        cbFreeShipping.visibility = View.VISIBLE
        cbFastShipping.visibility = View.VISIBLE
        cbBrandNew.visibility = View.VISIBLE
    }

    private fun showSecondhandFilters() {
        val sortOrderTitleSecondhand = ArrayList<String>()
        viewModel.sortOrdersSecondhand.forEach { sortOrderTitleSecondhand.add(getString(it.resourceId)) }

        spSortBy.adapter = ArrayAdapter<String>(
            this@FiltersFragment.requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            sortOrderTitleSecondhand
        )

        cbFreeShipping.visibility = View.VISIBLE
        cbFastShipping.visibility = View.GONE
        cbBrandNew.visibility = View.GONE
    }

    private fun setMallFilters() {
        viewModel.apply {
            rgMarketplace.check(rbMarketplaceMall.id)
            for (i in sortOrdersMall.indices) {
                if (sortOrdersMall[i].key == searchRequestDraft.sortBy) {
                    spSortBy.setSelection(i)
                    break
                }
            }
            cbFastShipping.isChecked = searchRequestDraft.fastShipping != 0
            cbFreeShipping.isChecked = searchRequestDraft.freeShipping != 0
            cbBrandNew.isChecked = searchRequestDraft.brandNew != 0
        }
    }

    private fun setSecondhandFilters() {
        viewModel.apply {
            rgMarketplace.check(rbMarketplaceSecondhand.id)
            for (i in sortOrdersSecondhand.indices) {
                if (sortOrdersSecondhand[i].key == searchRequestDraft.sortBy) {
                    spSortBy.setSelection(i)
                    break
                }
            }
            cbFastShipping.isChecked = searchRequestDraft.fastShipping != 0
            cbFreeShipping.isChecked = searchRequestDraft.freeShipping != 0
            cbBrandNew.isChecked = searchRequestDraft.brandNew != 0
        }
    }


    private fun setListeners() {
        setMarketplaceRgListener()
        setSortBySpListener()
        setApplyFiltersBtnListener()
    }

    private fun setMarketplaceRgListener() {
        rgMarketplace.setOnCheckedChangeListener { _, id ->
            viewModel.setDraftMarketplace(id == rbMarketplaceMall.id)
            // Each marketplace has its own set of filters so we must also reconfigure the UI
            updateFilters()
        }
    }

    private fun setSortBySpListener() {
        spSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.e(DEBUG_TAG, "spSortBy")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.apply {
                    if (isMall(searchRequestDraft)) {
                        searchRequestDraft.sortBy = sortOrdersMall[position].key
                    } else {
                        searchRequestDraft.sortBy = sortOrdersSecondhand[position].key
                    }
                }
            }
        }
    }

    private fun setApplyFiltersBtnListener() {
        btnApplyFilters.setOnClickListener {
            // Check any views that do not automatically update the search request
            viewModel.setDraftBinaryFilters(
                cbFreeShipping.isChecked,
                cbFastShipping.isChecked,
                cbBrandNew.isChecked
            )

            viewModel.applyFiltersAndSearch()
            findNavController().popBackStack()
        }
    }
}