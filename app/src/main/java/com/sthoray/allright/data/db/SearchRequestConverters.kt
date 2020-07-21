package com.sthoray.allright.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sthoray.allright.data.model.search.Location
import com.sthoray.allright.data.model.search.PropertyFilter

/** Type converters for data classes that will be stored in a Room database. */
class SearchRequestConverters {

    /**
     * Convert a [Location] from a search request into a string primitive.
     *
     * @param location The search [Location] to convert into a JSON string.
     *
     * @return The JSON string representing [location].
     */
    @TypeConverter
    fun fromLocation(location: Location): String {
        return Gson().toJson(location)
    }

    /**
     * Convert a string primitive into a search [Location] for a search request.
     *
     * @param location The JSON string representing a [Location].
     *
     * @return The [Location] contained in [location].
     */
    @TypeConverter
    fun toLocation(location: String): Location {
        return Gson().fromJson(location, Location::class.java)
    }


    /**
     * Convert a list of [PropertyFilter]s from a search request into a string primitive.
     *
     * @param propertyFilters The [PropertyFilter]s to convert into a JSON string.
     *
     * @return The JSON string representing [propertyFilters].
     */
    @TypeConverter
    fun fromPropertyFilters(propertyFilters: List<PropertyFilter>): String {
        return Gson().toJson(propertyFilters)
    }

    /**
     * Convert a string primitive into a list of [PropertyFilter]s for a search request.
     *
     * @param propertyFilters The JSON string representing the list of [PropertyFilter]s.
     *
     * @return The list of [PropertyFilter]s contained in [propertyFilters].
     */
    @TypeConverter
    fun toPropertyFilters(propertyFilters: String): List<PropertyFilter> {
        val listType = object : TypeToken<List<PropertyFilter>>() {}.type
        return Gson().fromJson(propertyFilters, listType)
    }
}