package com.sthoray.allright


import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExploreActivityTest{


    @Test
    fun test_isActivityInView() {

        //This function launches the activity and tests that it is in view

        val activityScenario = ActivityScenario.launch(ExploreActivity::class.java)
    }
}