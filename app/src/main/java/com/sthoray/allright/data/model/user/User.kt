package com.sthoray.allright.data.model.user

import com.google.gson.annotations.SerializedName

/**
 * Model for user data.
 *
 * Some fields have been omitted as they are currently not in use or have not
 * been verified for behaviour.
 *
 * @property id id The user's numeric ID.
 * @property firstName The user's first name.
 * @property lastName The user's last name.
 * @property image The user's profile image.
 * @property externalUsername The user's publicly identifiable username.
 * @property description The user's profile description. Null if not provided.
 * @property email The email address that the user authenticates and receives
 * communication with.
 * @property createdAt The datetime that the user registered at.
 * @property mobilePhone The user's contact number.
 * @property points This could be the number of listings or feedback rating.
 * This needs further investigation before being put into the UI.
 * @property locationName The user's publicly visible location.
 * @property lng The user's longitudinal coordinate.
 * @property lat The user's latitudinal coordinate.
 * @property verificationLevel The user's verification level. This description
 * requires additional expansion.
 * @property config Configuration details describing what UI should be visible
 * or accessible to user.
 */
data class User(
    val id: Int?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    val image: UserImage?,
    @SerializedName("external_username") val externalUsername: String?,
    val description: String?,
    val email: String?,
    // @SerializedName("class") val `class`: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("mobile_phone") val mobilePhone: String?,
    // @SerializedName("stripe_id") val stripeId: Any,
    // @SerializedName("car_brand") val cardBrand: Any,
    // @SerializedName("card_last_four") val card_last_four: Any,
    // @SerializedName("view_restricted") val viewRestricted: Any,
    val points: Int?,
    @SerializedName("location_name") val locationName: String?,
    val lng: String?,
    val lat: String?,
    @SerializedName("verification_level") val verificationLevel: Int?,
    val config: Config?
)
