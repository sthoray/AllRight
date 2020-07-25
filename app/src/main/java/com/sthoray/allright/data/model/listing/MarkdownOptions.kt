package com.sthoray.allright.data.model.listing

/**
 * Model for markdown options.
 *
 * This model describes how a listing's description field can be interpreted with
 * markdown syntax. This object should be checked to decide how to render the
 * the description on the screen.
 *
 * @property disabled False if [Listing.description] contains markdown syntax, false otherwise.
 */
data class MarkdownOptions(
    val disabled: Boolean
)