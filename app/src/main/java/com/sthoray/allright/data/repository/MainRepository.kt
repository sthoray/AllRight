package com.sthoray.allright.data.repository

import com.sthoray.allright.data.api.ApiHelper
import com.sthoray.allright.data.model.CategoryFeaturePanel

/**
 * Repository for handling data operations.
 *
 * Provides a clean API for the View Model to interact with to receive data.
 *
 * @property apiHelper the [ApiHelper] to work with
 */
class MainRepository(private val apiHelper: ApiHelper) {

    /**
     * Get featured categories panel.
     */
    suspend fun getCategoryFeaturePanel() = apiHelper.getCategoryFeaturePanel()
}