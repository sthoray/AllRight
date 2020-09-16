package com.sthoray.allright.data.model.user

/**
 * Model for a user avatar.
 *
 * Contains URLs for an avatar's full sized image and its thumbnail.
 *
 * @property large The fully qualified url to the full sized image.
 * @property thumb The fully qualified url to the image thumbnail.
 */
data class UserImage(
    val large: String?,
    val thumb: String?
)
