package com.sthoray.allright.ui.listing.view

import android.os.Bundle
import android.widget.ImageView
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.search.view.SearchActivity.Companion.LISTING_ID_KEY
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*

/** The listing activity to display information about a listing. */
class ListingActivity : AppCompatActivity() {


    private lateinit var viewModel: ListingViewModel
    private val TAG = "ListingActivity"

    var sampleImages = intArrayOf(
        R.drawable.image_1,
        R.drawable.image_2
        //R.drawable.image_3,
        //R.drawable.image_4,
        //R.drawable.image_5
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setupViewModel()
        viewModel.getListing(
            intent.getIntExtra(
                LISTING_ID_KEY,
                0
            )

        )
        setupObservers()

        // TODO: Open website as follows
        /*resultsAdapter.setOnItemClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.BASE_PRODUCT_URL + it.id)
            this.startActivity(intent)
        } */

        carouselView.apply {
            pageCount = sampleImages.size
            setImageListener(imageListener)
        }

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
                        textViewTitle.text = listing.productName
                        textViewDescription.text = listing.description
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

    private fun showProgressBar() {
        progBarListing.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progBarListing.visibility = View.GONE
    }

    private var imageListener: ImageListener = ImageListener { position, imageView ->
        imageView.setImageResource(sampleImages[position])
    }
}