package com.sthoray.allright.ui.search.view

import android.os.Bundle
import android.util.Log
import android.view.View
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
        showCurrentFilters()
        setupApplyFiltersBtn(view)
    }

    private fun showCurrentFilters() {
        viewModel.apply {
            resetDraftRequest()

            // Current marketplace
            if (isMall()) {
                rgMarketplace.check(rbMarketplaceMall.id)
            } else {
                rgMarketplace.check(rbMarketplaceSecondhand.id)
            }

            // TODO: Set sort order

            // Checkbox options
            searchRequest.apply {
                cbFastShipping.isChecked = fastShipping != 0
                cbFreeShipping.isChecked = freeShipping != 0
                cbBrandNew.isChecked = brandNew != 0
            }
        }
    }

    private fun setupApplyFiltersBtn(view: View) {
        btnApplyFilters.setOnClickListener {
            viewModel.apply {
                // Set marketplace
                val marketplace = view.findViewById<RadioButton>(rgMarketplace.checkedRadioButtonId)
                val isMall = marketplace.text.toString() == getString(R.string.marketplace_mall)
                draftMarketplace(isMall)

                // TODO: Set sort order

                // Checkbox options
                draftBinaryFilters(
                    cbFreeShipping.isChecked,
                    cbFastShipping.isChecked,
                    cbBrandNew.isChecked
                )
            }

            Log.d(TAG, "Applying filters")
            viewModel.applyDraftAndSearch()
        }
    }

}