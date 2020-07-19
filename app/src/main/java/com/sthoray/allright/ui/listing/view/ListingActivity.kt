package com.sthoray.allright.ui.listing.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.listing.adapter.ListingAdapter
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.ui.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*


class ListingActivity : AppCompatActivity() {

    private lateinit var viewModel: ListingViewModel
    private lateinit var listingAdapter: ListingAdapter
    private val TAG = "ListingActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setupViewModel()
        setupUI()
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ListingViewModel::class.java)
    }

    private fun setupUI() {
        listingAdapter = ListingAdapter()
    }

}