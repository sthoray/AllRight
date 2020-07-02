package com.sthoray.allright.data.api

import com.sthoray.allright.data.model.CategoryFeaturePanel

/**
 *  API Helper class to help with ApiService calls.
 *
 *  @property apiService
 */
class ApiHelper(private val apiService: ApiService) {

    /**
     * Get featured categories panel.
     */
    suspend fun getCategoryFeaturePanel() = apiService.getCategoryFeaturePanel()
}