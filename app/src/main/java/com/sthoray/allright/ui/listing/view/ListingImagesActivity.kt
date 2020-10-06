package com.sthoray.allright.ui.listing.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ViewPagerAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingImagesViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.vpListingImages
import kotlinx.android.synthetic.main.activity_listing.wdiListingImages
import kotlinx.android.synthetic.main.activity_listing_images.*
import timber.log.Timber

/** The listing images activity to display the images in full screen. */
class ListingImagesActivity : AppCompatActivity() {


    private lateinit var viewModel: ListingImagesViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var imagePosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_images)
        setupViewModel()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)
        imagePosition = intent.getIntExtra("ImagePosition", 0)

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
                        Toast.makeText(
                            this,
                            getString(R.string.error_occurred_preamble) + message,
                            Toast.LENGTH_LONG
                        ).show()
                        Timber.e("%s%s", getString(R.string.error_occurred_preamble), message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun fillView(listing: Listing) {
        listing.images?.let { setViewPager(it) }
    }

    private fun setViewPager(images: List<Image>) {
        viewPagerAdapter = ViewPagerAdapter(images)
        vpListingImages.adapter = viewPagerAdapter
        vpListingImages.setCurrentItem(imagePosition, false)
        wdiListingImages.setViewPager2(vpListingImages)

        // hack to prevent worm dots indicator from showing with the incorrect index
        vpListingImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                wdiListingImages.visibility = View.VISIBLE
            }
        })
    }

    private fun showProgressBar() {
        pbListingImages.visibility = View.VISIBLE
        vpListingImages.visibility = View.GONE
        wdiListingImages.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbListingImages.visibility = View.GONE
        vpListingImages.visibility = View.VISIBLE
        wdiListingImages.visibility = View.GONE
    }
}