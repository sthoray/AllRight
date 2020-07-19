package com.sthoray.allright.data.model.listing

import com.google.gson.annotations.SerializedName

/**
 * Model for the anything which is related to the store,
 * from which the product came.
 *
 * @property id the id.
 * @property store_id the id of the store.
 */
data class StoreRelated (
    val id: Int,
    val store_id: Int

)