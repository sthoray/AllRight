package com.sthoray.allright.ui.listing.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ViewPagerAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingImagesViewModel
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.utils.Constants.Companion.BASE_PRODUCT_URL
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.activity_listing.vpListingImages
import kotlinx.android.synthetic.main.activity_listing.wdiListingImages
import kotlinx.android.synthetic.main.activity_listing_images.*

/** The listing images activity to display the images in full screen. */
class ListingImagesActivity : AppCompatActivity() {


    private val TAG = "ListingImagesActivity"
    private lateinit var viewModel: ListingImagesViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_images)
        setupViewModel()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)

        setupObservers()

    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingImagesViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.listing.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { fillView(it) }
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

    private fun fillView(listing: Listing) {
        // Images
        listing.images?.let { setViewPager(it) }
    }



    private fun setViewPager(images: List<Image>) {

        viewPagerAdapter = ViewPagerAdapter(images)
        vpListingImages.adapter = viewPagerAdapter
        wdiListingImages.setViewPager2(vpListingImages)


    }


    private fun showProgressBar() {
        pbListingImages.visibility = View.VISIBLE
        vpListingImages.visibility = View.GONE
        wdiListingImages.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbListingImages.visibility = View.GONE
        vpListingImages.visibility = View.VISIBLE
        wdiListingImages.visibility = View.VISIBLE
    }
}