package com.sthoray.allright.ui.listing.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ViewPagerAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.utils.Constants.Companion.BASE_PRODUCT_URL
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*

/** The listing activity to display information about a listing. */
class ListingActivity : AppCompatActivity() {


    private val TAG = "ListingActivity"
    private lateinit var viewModel: ListingViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setupViewModel()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)
        setVisitListingBtnListener(listingId)

        setupObservers()
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.listing.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { listing ->
                        tvListingName.text = listing.name
                        tvListingDescription.text = listing.description
                        setViewPager(listing.images)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Log.e(TAG, "An error occurred $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setViewPager(images: List<Image>) {
        viewPagerAdapter = ViewPagerAdapter(images)
        vpListingImages.adapter = viewPagerAdapter
        wdiListingImages.setViewPager2(vpListingImages)
    }

    private fun setVisitListingBtnListener(listingId: Int) {
        btnVisitListing.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("$BASE_PRODUCT_URL$listingId")
            this.startActivity(intent)
        }
    }

    private fun showProgressBar() {
        pbListingDetails.visibility = View.VISIBLE
        vpListingImages.visibility = View.GONE
        wdiListingImages.visibility = View.GONE
        tvListingName.visibility = View.GONE
        tvListingSubtitle.visibility = View.GONE
        tvListingDescription.visibility = View.GONE
        btnVisitListing.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbListingDetails.visibility = View.GONE
        vpListingImages.visibility = View.VISIBLE
        wdiListingImages.visibility = View.VISIBLE
        tvListingName.visibility = View.VISIBLE
        tvListingSubtitle.visibility = View.VISIBLE
        tvListingDescription.visibility = View.VISIBLE
        btnVisitListing.visibility = View.VISIBLE
    }
}