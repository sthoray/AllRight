package com.sthoray.allright.data.model.browse


import com.google.gson.annotations.SerializedName

/**
 * Shortened category data model for parent categories.
 *
 * @property classX Unused / To be explored.
 * @property id The category's ID.
 * @property level The category's level down the tree. Level 0 corresponds to the root, i.e. all categories.
 * @property parent The parent category's ID. If the category is a top level category ([level] = 1), then the parent will be 0.
 * @property name The category's display name that can be presented to the user.
 * @property auctionable 1 if the contained listings can be auctions, else 0.
 * @property productable 1 if the contained listings can be products, else 0.
 * @property oversized 1 if the contained listings can be oversized, else 0.
 * @property customExplanation 1 if there is a custom explanation for this category, else 0.
 * @property isRestricted 1 if this category is restricted, else 0.
 */
data class CategoryParent(
    @SerializedName("class")
    val classX: Int?, // 0
    val id: Int?, // 3250
    val level: Int?, // 1
    val parent: Int?, // 0
    val name: String?, // Electronics & Computers
    val auctionable: Int?, // 1
    val productable: Int?, // 1
    val oversized: Int?, // 0
    @SerializedName("custom_explanation")
    val customExplanation: Int?, // 0
    @SerializedName("is_restricted")
    val isRestricted: Int? // 0
)