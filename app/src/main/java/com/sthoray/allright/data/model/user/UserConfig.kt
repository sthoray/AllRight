package com.sthoray.allright.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Data model for user configuration.
 *
 * @property uiHides Details what UI should be hidden from the user.
 */
data class Config(
    @SerializedName("ui_hides") val uiHides: UiHides?
)

/**
 * Data model for hidden UI views.
 *
 * Each property is a boolean. If true, the related UI view should be disabled
 * or hidden as it is not needed for the user. While this data class will
 * be unused for the initial releases, it will be come necessary as additional
 * features are added over time.
 *
 * @property nav True if the nav should be hidden, else false?
 * @property storemanagerDashboard True if the dashboard should be hidden,
 * else false.
 * @property storemanagerProductManager True if the product-manager should be
 * hidden, else false.
 * @property storemanagerOrders True if orders should be hidden, else false.
 * @property storemanagerPayments True if payments should be hidden, else
 * false.
 * @property storemanagerPromote True if promote should be hidden, else false.
 * @property storemanagerIntegrations True if integrations should be hidden,
 * else false.
 * @property storemanagerBanner True if the banner should be hidden, else
 * false.
 * @property storemanagerCarpartsManager True if the carparts-manager should be
 * hidden, else false.
 */
data class UiHides(
    val nav: Boolean?,
    @SerializedName("storemanager.dashboard") val storemanagerDashboard: Boolean?,
    @SerializedName("storemanager.product-manager") val storemanagerProductManager: Boolean?,
    @SerializedName("storemanager.orders") val storemanagerOrders: Boolean?,
    @SerializedName("storemanager.payments") val storemanagerPayments: Boolean?,
    @SerializedName("storemanager.promote") val storemanagerPromote: Boolean?,
    @SerializedName("storemanager.integrations") val storemanagerIntegrations: Boolean?,
    @SerializedName("storemanager.banner") val storemanagerBanner: Boolean?,
    @SerializedName("storemanager.carparts-manager") val storemanagerCarpartsManager: Boolean?
)
