package com.sthoray.allright

// TODO: Merge TopLevel and FeatureCategory together

// Browse model

/**
 * Top level category model.
 *
 * Stores basic information relating to a top level category. The properties are
 * used to display a top level category in the browse activity.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property listing_count the number of listings contained in the category
 * @property icon the name of the icon to represent the category
 */
class TopLevelCategory(val id: Int, val name: String, val listing_count: Int, val icon: String)




// Discover models

/**
 * Featured category panel model.
 *
 * Contains a list of categories to display in the feature panel. AllGoods are
 * unfortunately using integers object names for each category. To work around
 * this the categories property is a Map. It may be useful to convert this
 * property into a List before using it. e.g. ArrayList(myObj.categories.values)
 *
 * @property categories a map with [FeatureCategory] values
 */
class FeaturePanelCategory(val categories: Map<Int, FeatureCategory>)

/**
 * Featured category model.
 *
 * Contains information required to display a featured category. The properties are
 * used to display a featured category in the discover activity.
 *
 * @property id the category ID
 * @property name the name of the category
 * @property icon the name of the icon to represent the category
 * @property listing_count the number of listings contained in the category
 * @property image the url path to an associated image
 * @property url the url path to browse the category in a web browser
 */
class FeatureCategory(val id: Int,
                      val name: String,
                      val icon: String,
                      val listing_count: Int,
                      val image: String,
                      val url: String)
