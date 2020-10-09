package com.sthoray.allright.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import coil.load
import coil.transform.CircleCropTransformation
import com.sthoray.allright.R
import com.sthoray.allright.data.model.user.User
import com.sthoray.allright.ui.login.view.LoginActivity
import com.sthoray.allright.ui.main.viewmodel.MainViewModel
import com.sthoray.allright.utils.Constants.Companion.BASE_URL
import com.sthoray.allright.utils.EspressoIdlingResource
import com.sthoray.allright.utils.Resource
import kotlinx.android.synthetic.main.fragment_my_allgoods.*
import timber.log.Timber

/**
 * Fragment for viewing and interacting with the user's AllGoods account.
 */
class MyAllGoodsFragment : Fragment(R.layout.fragment_my_allgoods) {

    private lateinit var viewModel: MainViewModel

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

    /**
     * Fetch user data on resume if the login button was previously pressed.
     */
    override fun onResume() {
        super.onResume()

        var bearerToken: String? = null
        context?.let { context ->
            val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                context,
                getString(R.string.preference_crypt_auth_key),
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            bearerToken = encryptedSharedPreferences.getString(
                getString(R.string.user_bearer_token_key),
                null
            )
        }

        if (bearerToken.isNullOrEmpty() == viewModel.userProfileShown) {
            viewModel.getUserProfile()
        }
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
                            getString(R.string.error_occurred_preamble) + message,
                            Toast.LENGTH_LONG
                        ).show()
                        Timber.e("%s%s", getString(R.string.error_occurred_preamble), message)
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
        viewModel.userProfileShown = true
    }

    private fun hideAuthenticatedViews() {
        btnMyAllGoodsLogin.visibility = View.VISIBLE
        btnMyAllGoodsRegister.visibility = View.VISIBLE
        ivMyAllGoodsAvatar.visibility = View.GONE
        tvMyAllGoodsName.visibility = View.GONE
        tvMyAllGoodsEmail.visibility = View.GONE
        tvMyAllGoodsLocation.visibility = View.GONE
        viewModel.userProfileShown = false
    }

    private fun bindViews(user: User) {
        with(user) {
            tvMyAllGoodsName.text = getString(R.string.fullname_concatenate)
                .format(firstName, lastName)
            tvMyAllGoodsEmail.text = email
            tvMyAllGoodsLocation.text = locationName
            ivMyAllGoodsAvatar.load(image?.large) {
                transformations(CircleCropTransformation())
            }
        }
    }
}
