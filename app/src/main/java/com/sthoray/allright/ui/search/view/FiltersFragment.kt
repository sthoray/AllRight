package com.sthoray.allright.ui.search.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.ui.search.adapter.CategoryAdapter
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel.Companion.sortOrdersMall
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel.Companion.sortOrdersSecondhand
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_filters.*
import timber.log.Timber

/**
 * Search filtering fragment.
 *
 * This fragment provides a central place to view, modify, and remove all
 * filters for the current search request.
 */
class FiltersFragment : Fragment(R.layout.fragment_filters) {


    private lateinit var viewModel: SearchViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private var parentCategory: Int? = null


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
        viewModel.draftSearch()
        setupView()
        setupObservers()
    }

    private fun setupView() {
        setVisibleFilters()
        setupRecyclerViews()
        setupListeners()
    }

    private fun setVisibleFilters() {
        if (with(viewModel) { searchRequestDraft.isMall() }) {
            showMallFilters()
            setMallFilters()
        } else {
            showSecondhandFilters()
            setSecondhandFilters()
        }
        viewModel.draftSearch()
    }

    private fun setupRecyclerViews() {
        // Child categories
        categoryAdapter = CategoryAdapter()
        categoryAdapter.setOnItemClickListener { category ->
            if (category.isRestricted == 1) {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.restricted_category_title)
                    .setMessage(R.string.restricted_category_message)
                    .setPositiveButton(
                        R.string.restricted_category_continue,
                        DialogInterface.OnClickListener { _, _ ->
                            category.id?.let { viewModel.searchRequestDraft.categoryId = it }
                            viewModel.draftSearch()
                        })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            } else {
                category.id?.let { viewModel.searchRequestDraft.categoryId = it }
                viewModel.draftSearch()
            }
        }

        rvCategoryChildren.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        btnCategoryUp.setOnClickListener {
            viewModel.searchRequestDraft.categoryId = parentCategory ?: 0
            viewModel.draftSearch()
        }
    }

    private fun setupListeners() {
        // Marketplace radio group
        rgMarketplace.setOnCheckedChangeListener { _, id ->
            viewModel.setDraftMarketplace(id == rbMarketplaceMall.id)
            // Each marketplace has its own set of filters so we must also reconfigure the UI
            setVisibleFilters()
        }

        // Sort order spinner
        spSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Empty overridden function
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.apply {
                    if (searchRequestDraft.isMall()) {
                        searchRequestDraft.sortBy = sortOrdersMall[position].key
                    } else {
                        searchRequestDraft.sortBy = sortOrdersSecondhand[position].key
                    }
                }
            }
        }

        // Apply button
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

    private fun setupObservers() {
        // Category info
        viewModel.draftBrowseResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    pbChildCategoriesLoading.visibility = View.GONE
                    response.data?.apply {
                        children?.let { categoryAdapter.differ.submitList(it) }
                        category?.name?.let { tvCurrentCategory.text = it }
                        parentCategory = category?.parent
                        btnCategoryUp.isEnabled = !(parentCategory == null || parentCategory == 0)
                    }
                }
                is Resource.Error -> {
                    pbChildCategoriesLoading.visibility = View.GONE
                    response.message?.let {
                        tvCurrentCategory.text = it
                        Toast.makeText(
                            activity,
                            getString(R.string.error_occurred_preamble) + it,
                            Toast.LENGTH_LONG
                        ).show()
                        Timber.e("%s%s", getString(R.string.error_occurred_preamble), it)
                    }
                }
                is Resource.Loading -> {
                    pbChildCategoriesLoading.visibility = View.VISIBLE
                    btnCategoryUp.isEnabled = false // Prevent the user from spamming up
                }
            }
        })
    }

    private fun showMallFilters() {
        val sortOrderTitleMall = ArrayList<String>()
        sortOrdersMall.forEach { sortOrderTitleMall.add(getString(it.resourceId)) }

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
        sortOrdersSecondhand.forEach { sortOrderTitleSecondhand.add(getString(it.resourceId)) }

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
}
