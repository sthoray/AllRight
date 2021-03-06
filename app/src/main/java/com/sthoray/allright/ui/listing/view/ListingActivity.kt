package com.sthoray.allright.ui.listing.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Image
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ViewPagerAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.utils.Constants.Companion.BASE_PRODUCT_URL
import com.sthoray.allright.utils.EspressoIdlingResource
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*
import timber.log.Timber

/** The listing activity to display information about a listing. */
class ListingActivity : AppCompatActivity() {


    private lateinit var viewModel: ListingViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        setContentView(R.layout.activity_listing)
        setupViewModel()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)
        setVisitListingBtnListener(listingId)

        setupObservers()

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()

    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingViewModel::class.java)
    }

    private fun setupObservers() {
        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        viewModel.listing.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { fillView(it) }

                    //Decrement the idling resource for testing
                    EspressoIdlingResource.decrement()
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

    private fun setViewPager(images: List<Image>) {
        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        viewPagerAdapter = ViewPagerAdapter(images)
        vpListingImages.adapter = viewPagerAdapter
        wdiListingImages.setViewPager2(vpListingImages)

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()

    }

    //BENJAMIN
    private fun setOnClickListeners(listing: Listing) {

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        viewPagerAdapter.setOnItemClickListener { image ->
            val index = image.number?.let { it - 1 }
            Intent(this@ListingActivity, ListingImagesActivity::class.java).also {
                it.putExtra(LISTING_ID_KEY, listing.id)
                it.putExtra("ImagePosition", index)
                startActivity(it)
            }
        }
        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()
    }


    private fun setVisitListingBtnListener(listingId: Int) {
        btnVisitListing.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("$BASE_PRODUCT_URL$listingId")
            this.startActivity(intent)
        }
    }

    private fun fillView(listing: Listing) {
        //Increment the idling resource for testing
        EspressoIdlingResource.increment()

        // Description
        tvListingName.text = listing.name
        tvListingDescription.text = listing.description
        //Here we can access the listing variable and maybe save it to send to the ListingImagesActivity???
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
        //Item Specifics
        listing.properties?.let {
            if (it.size > 0) {
                var allProperties = String()
                for (p in it) {
                    allProperties += p.title
                    if (p.type == 1) {
                        allProperties += ": " + p.option + "\n"
                    } else if (p.type == 2) {
                        allProperties += ": " + p.value + "\n"
                    }
                }
                tvListingProperties.text = allProperties
                tvListingProperties.visibility = View.VISIBLE
                tvListingPropertiesTitle.visibility = View.VISIBLE
            }
        }
        // Seller's info
        listing.manager?.let { manager ->
            if (manager.storeName != null) {
                ivSellersImage.load(manager.logo?.thumbUrl) {
                    transformations(CircleCropTransformation())
                }
                tvSellersName.text = manager.storeName
                tvSellersLocation.text = manager.locationName
            } else {
                ivSellersImage.load(manager.avatar?.thumb) {
                    transformations(CircleCropTransformation())
                }
                tvSellersName.text = manager.firstName
                tvSellersLocation.text = manager.createdAt
            }
        }
        // Images
        listing.images?.let { setViewPager(it) }
        listing.let { setOnClickListeners(it) }

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()
    }

    private fun showProgressBar() {
        pbListingDetails.visibility = View.VISIBLE
        vpListingImages.visibility = View.GONE
        wdiListingImages.visibility = View.GONE
        tvListingName.visibility = View.GONE
        tvListingLocation.visibility = View.GONE
        tvListingDescription.visibility = View.GONE
        btnVisitListing.visibility = View.GONE
        tvListingStartPriceTitle.visibility = View.INVISIBLE // fix for individual buy now prices
        tvListingStartPrice.visibility = View.GONE
        tvListingBuyNowPriceTitle.visibility = View.GONE
        tvListingBuyNowPrice.visibility = View.GONE
        tvSellerDetails.visibility = View.GONE
        ivSellersImage.visibility = View.GONE
        tvSellersName.visibility = View.GONE
        tvSellersLocation.visibility = View.GONE
        tvListingProperties.visibility = View.GONE
        tvListingPropertiesTitle.visibility = View.GONE
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