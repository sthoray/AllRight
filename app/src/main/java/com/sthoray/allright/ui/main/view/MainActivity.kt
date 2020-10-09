package com.sthoray.allright.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sthoray.allright.R
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.repository.AppRepository
import com.sthoray.allright.ui.base.ViewModelProviderFactory
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.ui.settings.view.SettingsActivity
import com.sthoray.allright.utils.EspressoIdlingResource
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The main activity where everything starts.
 *
 * Contains a bottom navigation bar to switch between commonly
 * used fragments.
 */
class MainActivity : AppCompatActivity() {

    companion object {

        /** The key for a selected categoryId. */
        const val CATEGORY_ID_KEY = "CATEGORY_ID"
    }

    /**
     * Main activity's shared view model.
     *
     * By setting up an activity level view model and accessing it in
     * each main fragment, we can reduce the number of API calls.
     */
    lateinit var viewModel: MainViewModel

    /**
     * Set up navigation bar.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Increment the idling resource for testing
        EspressoIdlingResource.increment()



        setContentView(R.layout.activity_main)
        setupViewModel()
        setupView()

        //Decrement the idling resource for testing
        EspressoIdlingResource.decrement()
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(SearchHistoryDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
    }

    private fun setupView() {


        // Bottom Nav
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navigationHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        // Toolbar
        toolbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toolbarSettings -> {
                    Intent(this, SettingsActivity::class.java).also {
                        startActivity(it)
                    }


                    true
                }
                else -> false
            }
        }
    }
}

