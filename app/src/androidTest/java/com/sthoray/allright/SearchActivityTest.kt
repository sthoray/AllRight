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
class SearchActivityTest{

    @get: Rule
    val activityRule = ActivityScenarioRule(SearchActivity::class.java)

    @Test
    fun test_isActivityInView() {

        //This function launches the activity and tests that it is in view
        onView(withId(R.id.searchActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_searchResults() {
        //This function tests the visibility of the searchResults
        onView(withId(R.id.recyclerView_searchResults))
            .check(matches(isDisplayed()))
    }

}