package com.sthoray.allright.ui.listing.view

import android.os.Bundle
import android.widget.ImageView
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ListingAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.main.adapter.MainAdapter
import com.sthoray.allright.ui.main.view.MainActivity
import com.sthoray.allright.ui.search.view.SearchActivity
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.activity_main.*


class ListingActivity : AppCompatActivity() {

    private lateinit var viewModel: ListingViewModel
    private lateinit var listingAdapter: ListingAdapter
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
                SearchActivity.LISTING_ID_KEY,
                0
            )

        )
        setupObservers()

        val carouselView = findViewById(R.id.carouselView) as CarouselView
        carouselView.setPageCount(sampleImages.size)
        carouselView.setImageListener(imageListener)
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.listing.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { listing ->
                        textViewTitle.text = listing.productName
                        textViewSubtitle.text = listing.productName
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

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }
    }

    private fun hideProgressBar() {
        progBarListing.visibility = View.GONE
    }
}