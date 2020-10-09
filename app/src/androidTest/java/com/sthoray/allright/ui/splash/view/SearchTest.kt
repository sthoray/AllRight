package com.sthoray.allright.ui.splash.view


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sthoray.allright.R
import com.sthoray.allright.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun searchTest() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        val recyclerView = onView(
            allOf(
                withId(R.id.rvFeaturedCategories),
                childAtPosition(
                    withId(R.id.srlHome),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        onView(withId(R.id.search)).check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withText("Search"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarSearch),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withText("Search"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarSearch),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Search")))

        val viewGroup = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.rvSearchResults),
                        childAtPosition(
                            withId(R.id.srlSearchResults),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))


    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
