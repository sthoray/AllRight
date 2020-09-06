package com.sthoray.allright.ui.listing.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.result.view.SearchActivity.Companion.LISTING_ID_KEY
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
        setupActionBar()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)
        setVisitListingBtnListener(listingId)

        setupObservers()
    }
    private fun setupActionBar(){
        setSupportActionBar(findViewById(R.id.listing_toolbar))
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_button_arrow_back_white_24))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    private fun fillView(listing: Listing) {
        // Description
        tvListingName.text = listing.name
        tvListingDescription.text = listing.description
        listing.locationName?.let {
            tvListingLocation.text = it
            tvListingLocation.visibility = View.VISIBLE
        }

        // Price
        if (listing.buyNowPrice == null && listing.currentPrice == null) {
            // Likely a mall listing
            tvListingBuyNowPrice.text = String.format(
                getString(R.string.format_price),
                listing.startPrice
            )
            tvListingBuyNowPriceTitle.visibility = View.VISIBLE
            tvListingBuyNowPrice.visibility = View.VISIBLE
        } else {
            // If bidding has started, currentPrice might be more appropriate
            // If startPrice == buyNowPrice, tvStartPrice can probably be hidden
            listing.startPrice?.let {
                tvListingStartPrice.text = String.format(
                    getString(R.string.format_price),
                    it
                )
                tvListingStartPriceTitle.visibility = View.VISIBLE
                tvListingStartPrice.visibility = View.VISIBLE
            }
            listing.buyNowPrice?.let {
                tvListingBuyNowPrice.text = String.format(
                    getString(R.string.format_price),
                    it
                )
                tvListingBuyNowPriceTitle.visibility = View.VISIBLE
                tvListingBuyNowPrice.visibility = View.VISIBLE
            }
        }

        // Seller's info
        listing.manager?.let { manager ->
            if (manager.storeName != null) {
                ivSellersImage.load(manager.logo?.thumbUrl)
                tvSellersName.text = manager.storeName
                tvSellersLocation.text = manager.locationName
            } else {
                ivSellersImage.load(manager.avatar?.thumb)
                tvSellersName.text = manager.firstName
                tvSellersLocation.text = manager.createdAt
            }
        }

        // Images
        listing.images?.let { setViewPager(it) }
    }

    private fun showProgressBar() {
        pbListingDetails.visibility = View.VISIBLE
        vpListingImages.visibility = View.GONE
        wdiListingImages.visibility = View.GONE
        tvListingName.visibility = View.GONE
        tvListingLocation.visibility = View.GONE
        tvListingDescription.visibility = View.GONE
        btnVisitListing.visibility = View.GONE
        tvListingStartPriceTitle.visibility = View.INVISIBLE // Allows buy now price views be displayed individually
        tvListingStartPrice.visibility = View.GONE
        tvListingBuyNowPriceTitle.visibility = View.GONE
        tvListingBuyNowPrice.visibility = View.GONE
        tvSellerDetails.visibility = View.GONE
        ivSellersImage.visibility = View.GONE
        tvSellersName.visibility = View.GONE
        tvSellersLocation.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbListingDetails.visibility = View.GONE
        vpListingImages.visibility = View.VISIBLE
        wdiListingImages.visibility = View.VISIBLE
        tvListingName.visibility = View.VISIBLE
        tvListingDescription.visibility = View.VISIBLE
        btnVisitListing.visibility = View.VISIBLE
        tvSellerDetails.visibility = View.VISIBLE
        ivSellersImage.visibility = View.VISIBLE
        tvSellersName.visibility = View.VISIBLE
        tvSellersLocation.visibility = View.VISIBLE
    }
}