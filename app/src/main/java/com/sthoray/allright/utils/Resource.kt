package com.sthoray.allright.utils

/**
 * Utility to communicate the current state of Network Calls to the UI layer.
 *
 * @property status the status of the resource
 * @property data the actual data stored in the resource
 * @property message the message if an error occurs
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        /** A resource that has been retrieved. */
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        /** A resource that has not been retrieved successfully. */
        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        /** A resource that is currently being retrieved. */
        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)
    }
}