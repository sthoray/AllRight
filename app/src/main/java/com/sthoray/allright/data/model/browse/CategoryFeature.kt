package com.sthoray.allright.data.model.browse


import com.google.gson.annotations.SerializedName

/**
 * Featured panel category data model.
 *
 * @property imagePath The URL path to an associated image to display in the featured panel.
 * @property name The category's display name that can be presented to the user.
 * @property size Unused / To be explored.
 * @property id The category's ID.
 */
data class CategoryFeature(
    @SerializedName("image")
    val imagePath: String?, // img/browse/electronics/Artboard_7.jpg
    val name: String?, // Computers
    val size: Int?, // 3
    val id: Int? // 3251
)