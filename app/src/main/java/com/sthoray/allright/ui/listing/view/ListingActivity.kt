package com.sthoray.allright.ui.listing.view

import android.icu.text.AlphabeticIndex
import android.os.Bundle
import android.widget.ImageView
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.squareup.picasso.Picasso
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Image
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

    var imageUrls: List<String> = listOf()


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
                        imageUrls = createImageUrlList(listing.images)
                        Log.d(TAG, imageUrls.toString())
                        carouselView.apply {

                            pageCount = imageUrls.size
                            setImageListener(imageListener)


                        }
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

    private fun createImageUrlList(images: List<Image>): ArrayList<String> {
        val imageList: ArrayList<String> = arrayListOf()
        for (image in images) {
            imageList.add(image.largeUrl)
        }
        return imageList
    }

    private fun showProgressBar() {
        progBarListing.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progBarListing.visibility = View.GONE
    }



    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            Picasso.get().load(imageUrls[position]).into(imageView)
        }
    }

}