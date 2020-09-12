package com.sthoray.allright.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.sthoray.allright.R
import com.sthoray.allright.data.model.user.User
import com.sthoray.allright.ui.login.view.LoginActivity
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.utils.Constants.Companion.BASE_URL
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_my_allgoods.*

/**
 * Fragment for viewing and interacting with the user's AllGoods account.
 */
class MyAllGoodsFragment : Fragment(R.layout.fragment_my_allgoods) {

    private lateinit var viewModel: MainViewModel
    private val TAG = "HomeFragment"

    /**
     * Set up ViewModel, UI, and observers.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setOnClickListeners()
        setupObservers()
    }

    private fun setOnClickListeners() {
        btnMyAllGoodsLogin.setOnClickListener {
            Intent(activity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        btnMyAllGoodsRegister.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(BASE_URL)
                startActivity(it)
            }
        }
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data == null) {
                        hideAuthenticatedViews()
                    } else {
                        bindViews(response.data)
                        showAuthenticatedViews()
                    }
                }
                is Resource.Error -> {
                    hideAuthenticatedViews()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity,
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    hideAuthenticatedViews()
                }
            }
        })
    }

    private fun showAuthenticatedViews() {
        btnMyAllGoodsLogin.visibility = View.GONE
        btnMyAllGoodsRegister.visibility = View.GONE
        ivMyAllGoodsAvatar.visibility = View.VISIBLE
        tvMyAllGoodsName.visibility = View.VISIBLE
        tvMyAllGoodsEmail.visibility = View.VISIBLE
        tvMyAllGoodsLocation.visibility = View.VISIBLE
    }

    private fun hideAuthenticatedViews() {
        btnMyAllGoodsLogin.visibility = View.VISIBLE
        btnMyAllGoodsRegister.visibility = View.VISIBLE
        ivMyAllGoodsAvatar.visibility = View.GONE
        tvMyAllGoodsName.visibility = View.GONE
        tvMyAllGoodsEmail.visibility = View.GONE
        tvMyAllGoodsLocation.visibility = View.GONE
    }

    private fun bindViews(user: User) {
        with(user) {
            tvMyAllGoodsName.text = getString(R.string.fullname_concatenate)
                .format(firstName, lastName)
            tvMyAllGoodsEmail.text = email
            tvMyAllGoodsLocation.text = locationName
            ivMyAllGoodsAvatar.load(image?.large)
        }
    }
}
