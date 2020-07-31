package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Data class for a property / item specific.
 *
 * This data class excludes two properties which may not be useful for this app.
 *
 * @property id The ID of the property.
 * @property title The name of the item specific, e.g. Memory, screen resolution, etc.
 * @property type The type of the item specific. When 1, the [option] property should be used for the property description, when 2, the [value] property should be used.
 * @property option The description of the property if it is of [type] 1.
 * @property optionId The ID for the specified [option].
 * @property value The value of the property if it is of [type] 2.
 */
data class Property(
    val id: Int?,
    val title: String?,
    val type: Int?,
    val option: String?,
    @SerializedName("option_id")
    val optionId: Int?,
    val value: String?
)