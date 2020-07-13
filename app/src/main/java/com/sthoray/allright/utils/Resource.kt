package com.sthoray.allright.utils

/**
 * Generic resource to wrap around network responses.
 *
 * Allows us differentiate between successful and failed responses. Used
 * to communicate the current state of Network Calls to the UI component.
 *
 * @property data any data returned from a response
 * @property message error message returned from a failed response
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /** A successfully retrieved resource. */
    class Success<T>(data: T?) : Resource<T>(data)

    /** A unsuccessfully retrieved resource. */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /** A resource that is currently being retrieved. */
    class Loading<T>() : Resource<T>()
}