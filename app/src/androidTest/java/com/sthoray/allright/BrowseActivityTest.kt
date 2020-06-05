package com.sthoray.allright

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BrowseActivityTest{

    @get: Rule
    val activityRule = ActivityScenarioRule(BrowseActivity::class.java)

    @Test
    fun test_isActivityInView() {

        //This function launches the activity and tests that it is in view
        onView(withId(R.id.browseActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_topLevel() {
        //This function tests the visibility of the topLevel
        onView(withId(R.id.recyclerView_topLevel))
            .check(matches(isDisplayed()))
    }

}