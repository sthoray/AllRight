package com.sthoray.allright.utils

/**
 * Status to represent a [Resource] State.
 */
enum class Status {

    /** Operation completed successfully. */
    SUCCESS,

    /** Operation failed with an error. */
    ERROR,

    /** Operation in progress. */
    LOADING
}