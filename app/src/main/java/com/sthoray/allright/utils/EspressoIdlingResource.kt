package com.sthoray.allright.utils

import androidx.test.espresso.idling.CountingIdlingResource


/**
 * The Espresso Idling Resource for delaying the UI tests.
 * Involves incrementing and decrementing a counter to pause UI testing.
 * This forces the tests to wait until the data is loaded in the activities
 * before the data fields are tested by Espresso.
 */
object EspressoIdlingResource {


    private const val RESOURCE = "GLOBAL"

    /**
     * A counter which stops the Espresso tests from continuing while
     * it is above zero. When the counter reaches zero, the tests
     * will continue. Allows code to be executed before testing
     * UI components
     */
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    /**
     * Increments the counter, which stops Espresso from continuing to test.
     */
    fun increment() {
        countingIdlingResource.increment()
    }

    /**
     * Decrements the counter, which allows Espresso to continue testing.
     */
    fun decrement() {
        if (!countingIdlingResource.isIdleNow)
            countingIdlingResource.decrement()
    }
}