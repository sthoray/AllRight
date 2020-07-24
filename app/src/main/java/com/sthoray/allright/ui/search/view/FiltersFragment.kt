package com.sthoray.allright.ui.search.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.sthoray.allright.R
import com.sthoray.allright.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_filters.*

/**
 * Search filtering fragment.
 *
 * This fragment provides a central place to view, modify, and remove all
 * filters for the current search request.
 */
class FiltersFragment : Fragment(R.layout.fragment_filters) {


    private lateinit var viewModel: SearchViewModel
    private val TAG = "FiltersFragment"


    /**
     * Set up ViewModel, UI, and observers.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as SearchActivity).viewModel
        viewModel.resetDraftRequest()
        displayDraftFilters()
        setupApplyFiltersBtn(view)
    }


    /* Ugh! This is asking to be rewritten. */
    private fun displayDraftFilters() {
        val sortOrderTitleMall = ArrayList<String>()
        val sortOrderTitleSecondhand = ArrayList<String>()

        viewModel.sortOrdersMall.forEach { sortOrderTitleMall.add(getString(it.resourceId)) }
        viewModel.sortOrdersSecondhand.forEach { sortOrderTitleSecondhand.add(getString(it.resourceId)) }

        viewModel.apply {
            if (isDraftMall()) {
                // Marketplace
                rgMarketplace.check(rbMarketplaceMall.id)

                // Sort order
                spSortBy.apply {
                    adapter = ArrayAdapter<String>(
                        this@FiltersFragment.requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        sortOrderTitleMall
                    )

                    for (i in sortOrdersMall.indices) {
                        if (sortOrdersMall[i].key == searchRequestDraft.sortBy) {
                            setSelection(i)
                            break
                        }
                    }
                }
            } else {
                // Marketplace
                rgMarketplace.check(rbMarketplaceSecondhand.id)

                // Sort order
                spSortBy.apply {
                    adapter = ArrayAdapter<String>(
                        this@FiltersFragment.requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        sortOrderTitleSecondhand
                    )

                    for (i in sortOrdersSecondhand.indices) {
                        if (sortOrdersSecondhand[i].key == searchRequestDraft.sortBy) {
                            setSelection(i)
                            break
                        }
                    }
                }
            }

            // Quick checkboxes
            searchRequest.apply {
                cbFastShipping.isChecked = fastShipping != 0
                cbFreeShipping.isChecked = freeShipping != 0
                cbBrandNew.isChecked = brandNew != 0
            }
        }
    }


    /* This could also do with some cleaning up! */
    private fun setupApplyFiltersBtn(view: View) {
        btnApplyFilters.setOnClickListener {
            viewModel.apply {
                // Set marketplace
                val marketplace = view.findViewById<RadioButton>(rgMarketplace.checkedRadioButtonId)
                val isMall = marketplace.text.toString() == getString(R.string.marketplace_mall)
                draftMarketplace(isMall)

                // Sort order
                if (isMall()) {
                    when (spSortBy.selectedItem.toString()) {
                        getString(sortOrdersMall[0].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[0].key
                        getString(sortOrdersMall[1].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[1].key
                        getString(sortOrdersMall[2].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[2].key
                        getString(sortOrdersMall[3].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[3].key
                        getString(sortOrdersMall[4].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[4].key
                        getString(sortOrdersMall[5].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[5].key
                        getString(sortOrdersMall[6].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[6].key
                        getString(sortOrdersMall[7].resourceId) -> searchRequestDraft.sortBy = sortOrdersMall[7].key
                        else -> Log.e(TAG, "Invalid mall sort option")
                    }
                } else {
                    when (spSortBy.selectedItem.toString()) {
                        getString(sortOrdersSecondhand[0].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[0].key
                        getString(sortOrdersSecondhand[1].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[1].key
                        getString(sortOrdersSecondhand[2].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[2].key
                        getString(sortOrdersSecondhand[3].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[3].key
                        getString(sortOrdersSecondhand[4].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[4].key
                        getString(sortOrdersSecondhand[5].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[5].key
                        getString(sortOrdersSecondhand[6].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[6].key
                        getString(sortOrdersSecondhand[7].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[7].key
                        getString(sortOrdersSecondhand[8].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[8].key
                        getString(sortOrdersSecondhand[9].resourceId) -> searchRequestDraft.sortBy = sortOrdersSecondhand[9].key
                        else -> Log.e(TAG, "Invalid secondhand sort option")
                    }
                }

                // Checkbox options
                draftBinaryFilters(
                    cbFreeShipping.isChecked,
                    cbFastShipping.isChecked,
                    cbBrandNew.isChecked
                )
            }

            viewModel.applyDraftAndSearch()
        }
    }

}