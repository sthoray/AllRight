package com.sthoray.allright.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sthoray.allright.data.model.search.MainImage
import com.sthoray.allright.data.model.search.ShippingOption

/**
 * Type converters to convert between kotlin objects and primitives.
 */
class Converters {

    /**
     * Convert shipping options into a primitive string for use in a database.
     *
     * @param shippingOptions a list of [ShippingOption]s to convert
     *
     * @return a JSON string representing the list
     */
    @TypeConverter
    fun fromShippingOptions(shippingOptions: List<ShippingOption>): String {
        return Gson().toJson(shippingOptions)
    }

    /**
     * Convert a JSON string into a list of shipping options.
     *
     * @param shippingOptions a JSON string to convert
     *
     * @return a list of [ShippingOption]s
     */
    @TypeConverter
    fun toShippingOptions(shippingOptions: String): List<ShippingOption> {
        val listType = object : TypeToken<List<ShippingOption>>() { }.type
        return Gson().fromJson(shippingOptions, listType)
    }

    /**
     * Convert a main image into a primitive string for use in a database.
     *
     * @param mainImage a [MainImage] to convert
     *
     * @return a JSON string representing the main image
     */
    @TypeConverter
    fun fromMainImage(mainImage: MainImage): String {
        return Gson().toJson(mainImage)
    }

    /**
     * Convert a JSON string into a main image object.
     *
     * @param mainImage a JSON string to convert
     *
     * @return a [MainImage] object
     */
    @TypeConverter
    fun toMainImage(mainImage: String): MainImage {
        return Gson().fromJson(mainImage, MainImage::class.java)
    }
}
