package com.sthoray.allright.ui.listing.view

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
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*

/** The listing activity to display information about a listing. */
class ListingActivity : AppCompatActivity() {


    private val TAG = "ListingActivity"
    private lateinit var viewModel: ListingViewModel
    private lateinit var imageUrls: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setupViewModel()
        setupObservers()

        val listingId = intent.getIntExtra(LISTING_ID_KEY, 0)
        viewModel.getListing(listingId)

        // TODO: Open website
        /*resultsAdapter.setOnItemClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.BASE_PRODUCT_URL + it.id)
            this.startActivity(intent)
        } */
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.listing.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { listing ->
                        tvListingName.text = listing.productName
                        tvListingDescription.text = listing.description
                        imageUrls = getImageUrls(listing.images)
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

    private fun getImageUrls(images: List<Image>): List<String> {
        val imageList = mutableListOf<String>()
        for (image in images) {
            imageList.add(image.largeUrl)
        }
        return imageList
    }

    private fun showProgressBar() {
        pbListingDetails.visibility = View.VISIBLE
        vpProductImages.visibility = View.GONE
        tvListingName.visibility = View.GONE
        tvListingSubtitle.visibility = View.GONE
        tvListingDescription.visibility = View.GONE
    }

    private fun hideProgressBar() {
        pbListingDetails.visibility = View.GONE
        vpProductImages.visibility = View.VISIBLE
        tvListingName.visibility = View.VISIBLE
        tvListingSubtitle.visibility = View.VISIBLE
        tvListingDescription.visibility = View.VISIBLE
    }
}