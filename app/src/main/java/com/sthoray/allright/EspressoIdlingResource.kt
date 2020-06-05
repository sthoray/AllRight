package com.sthoray.allright

import androidx.test.espresso.idling.CountingIdlingResource

//Object to keep track of whether background processes are running while testing to make sure
//views aren't tested before they are properly populated
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    //Increments the counter if background processes are running
    fun increment(){
        countingIdlingResource.increment()
    }

    //Decrements the counter when background processes stop
    fun decrement(){
        if(!countingIdlingResource.isIdleNow){
            countingIdlingResource.decrement()
        }
    }


}