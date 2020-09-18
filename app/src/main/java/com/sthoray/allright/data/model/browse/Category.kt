package com.sthoray.allright.data.model.browse


import com.google.gson.annotations.SerializedName

/**
 * Full length category data model.
 *
 * @property id The category's ID.
 * @property name The category's display name that can be presented to the user.
 * @property icon The name of the icon to associate with this category on the web.
 * @property owner Unused / To be explored.
 * @property parent The parent category's ID. If the category is a top level category ([level] = 1), then the parent will be 0.
 * @property display Unused / To be explored.
 * @property level The category's level down the tree. Level 0 corresponds to the root, i.e. all categories.
 * @property auctionable 1 if the contained listings can be auctions, else 0.
 * @property productable 1 if the contained listings can be products, else 0.
 * @property oversized 1 if the contained listings can be oversized, else 0.
 * @property customExplanation 1 if there is a custom explanation for this category, else 0.
 * @property classX Unused / To be explored.
 * @property state Unused / To be explored.
 * @property value Unused / To be explored.
 * @property createdAt The timestamp that this category was created on.
 * @property updatedAt The timestamp that this category was last updated on.
 * @property isRestricted 1 if this category is restricted, else 0.
 * @property quality Unused / To be explored.
 * @property appIcon The name of the icon to associate with this category in the mobile app.
 * @property listingCount The number of listings contained in this category.
 * @property auctionCount The number of auctions contained in this category.
 * @property productCount The number of products contained in this category.
 * @property riskLevel Unused / To be explored.
 * @property showGoodAsNewInSecondhand Unused / To be explored.
 * @property defaultFreeShipping Unused / To be explored.
 * @property defaultFastShipping Unused / To be explored.
 * @property defaultBrandNew Unused / To be explored.
 * @property defaultLayoutList 1 if the items in this category should be displayed in a list by default, else 0.
 */
data class Category(
    val id: Int?, // 3250
    val name: String?, // Electronics & Computers
    val icon: String?, // fa fa-laptop
    val owner: Int?, // 1
    val parent: Int?, // 0
    val display: Int?, // 0
    val level: Int?, // 1
    val auctionable: Int?, // 1
    val productable: Int?, // 1
    val oversized: Int?, // 0
    @SerializedName("custom_explanation")
    val customExplanation: Int?, // 0
    @SerializedName("class")
    val classX: Int?, // 0
    val state: Int?, // 0
    val value: Int?, // 0
    @SerializedName("created_at")
    val createdAt: String?, // 2017-10-28 23:55:08
    @SerializedName("updated_at")
    val updatedAt: String?, // 2020-09-16 07:11:56
    @SerializedName("is_restricted")
    val isRestricted: Int?, // 0
    val quality: Int?, // 98929
    @SerializedName("app_icon")
    val appIcon: String?, // laptop-phone
    @SerializedName("listing_count")
    val listingCount: Int?, // 15194
    @SerializedName("auction_count")
    val auctionCount: Int?, // 3116
    @SerializedName("product_count")
    val productCount: Int?, // 12078
    @SerializedName("risk_level")
    val riskLevel: Int?, // 1
    @SerializedName("show_good_as_new_in_secondhand")
    val showGoodAsNewInSecondhand: Int?, // 0
    @SerializedName("default_free_shipping")
    val defaultFreeShipping: Int?, // 0
    @SerializedName("default_fast_shipping")
    val defaultFastShipping: Int?, // 0
    @SerializedName("default_brand_new")
    val defaultBrandNew: Int?, // 0
    @SerializedName("default_layout_list")
    val defaultLayoutList: Int? // 0
)