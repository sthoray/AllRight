package com.sthoray.allright

/**
 * Top level category model.
 *
 * Stores all basic information relating to a top level category. The properties
 * are used to display the contained top level category for the browse activity.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property listing_count the number of listings contained in the category
 * @property icon the name of the icon to represent the category
 */
class TopLevelCategory(val id: Int, val name: String, val listing_count: Int, val icon: String)
