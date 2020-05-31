package com.sthoray.allright


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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

        onView(withId(R.id.exploreActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_categories() {
        //This function test the visibility of the categories

        val activityScenario = ActivityScenario.launch(ExploreActivity::class.java)

        onView(withId(R.id.recyclerView_featuredCategories))
            .check(matches(isDisplayed()))
    }
}