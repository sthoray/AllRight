package com.sthoray.allright.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sthoray.allright.R
import com.sthoray.allright.data.db.AppDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.main.adapter.MainAdapter
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.search.view.SearchActivity
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The main activity where everything starts.
 *
 * This is the "home page" where the user us presented with suggestions to
 * start searching and can view their past search queries.
 */
class MainActivity : AppCompatActivity() {


    /** The ViewModel to interact with data. */
    private lateinit var viewModel: MainViewModel

    /** The adapter for displaying retrieved data. */
    private lateinit var mainAdapter: MainAdapter

    private val TAG = "MainActivity"


    /**
     * Set up ViewModel, UI, and observers when the activity is created.
     *
     * @param savedInstanceState and data saved if the activity is being re-initialized
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
    }


    /** Initialise the View Model for this activity. */
    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(this))
        val viewModelProviderFactory =
            ViewModelProviderFactory(
                appRepository
            )
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(MainViewModel::class.java)
    }

    /** Setup the UI. */
    private fun setupUI() {
        mainAdapter = MainAdapter()
        recViewMain.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        mainAdapter.setOnItemClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(CATEGORY_ID_KEY, it.id)
            this.startActivity(intent)
        }
    }

    /**  Subscribe to observable data and define View behaviour. */
    private fun setupObservers() {
        viewModel.featureCategories.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    removeProgressBar()
                    response.data?.let { featureCategoriesResponse ->
                        val categories = featureCategoriesResponse.categories.values.toList()
                        mainAdapter.differ.submitList(categories)
                    }
                }
                is Resource.Error -> {
                    removeProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        progBarMainPagination.visibility = View.VISIBLE
    }

    private fun removeProgressBar() {
        progBarMainPagination.visibility = View.GONE
    }

    companion object {

        /** The key for the selected categoryId. */
        const val CATEGORY_ID_KEY = "CATEGORY_ID"
    }
}