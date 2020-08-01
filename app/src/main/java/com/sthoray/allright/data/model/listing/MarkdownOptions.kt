package com.sthoray.allright.data.model.listing

/**
 * Model for markdown options.
 *
 * This model describes how a listing's description field can be interpreted with
 * markdown syntax. This object should be checked to decide how to render the
 * the description in a text view.
 *
 * @property disabled False if [Listing.description] contains markdown syntax, true otherwise.
 */
data class MarkdownOptions(
    val disabled: Boolean?
)