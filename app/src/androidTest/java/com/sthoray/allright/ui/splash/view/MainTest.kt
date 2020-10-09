package com.sthoray.allright.ui.splash.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class MainTest {

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
    fun mainTest() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        val textView = onView(
            allOf(
                withText("AllRight"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarMain),
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
                withText("AllRight"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarMain),
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
        textView2.check(matches(withText("AllRight")))

        val textView3 = onView(
            allOf(
                withId(R.id.toolbarSettings), withContentDescription("Settings"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbarMain),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))

        val viewGroup = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.rvFeaturedCategories),
                        childAtPosition(
                            withId(R.id.srlHome),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val frameLayout = onView(
            allOf(
                withId(R.id.navigation_home), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(R.id.navigation_browse), withContentDescription("Browse"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))
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
