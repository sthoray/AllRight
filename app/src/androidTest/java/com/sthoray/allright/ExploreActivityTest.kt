package com.sthoray.allright


import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExploreActivityTest{

    @get: Rule
    val activityRule = ActivityScenarioRule(ExploreActivity::class.java)

    val LIST_ITEM_IN_TEST = 0

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }



    @Test
    fun test_isActivityInView() {

        //This function launches the activity and tests that it is in view
        onView(withId(R.id.exploreActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_categories() {
        //This function tests the visibility of the categories
        onView(withId(R.id.recyclerView_featuredCategories))
            .check(matches(isDisplayed()))
    }

   @Test
   fun test_navSearchActivity() {
        //This function tests the navigation to the SearchActivity


        onView(withId(R.id.recyclerView_featuredCategories)).perform(actionOnItemAtPosition<FeaturedCategoryViewHolder>(LIST_ITEM_IN_TEST, click()))

        onView(withId(R.id.searchActivity))
            .check(matches(isDisplayed()))
    }
}