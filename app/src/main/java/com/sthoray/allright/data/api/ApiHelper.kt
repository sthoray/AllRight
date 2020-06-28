package com.sthoray.allright.data.api

/**
 *  API Helper class to help with ApiService calls.
 *
 *  @property apiService
 */
class ApiHelper(private val apiService: ApiService) {

    suspend fun getCategoryFeaturePanel() = apiService.getCategoryFeaturePanel()
}