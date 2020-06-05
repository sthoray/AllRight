package com.sthoray.allright

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ExploreActivityTest::class,
    SearchActivityTest::class,
    BrowseActivityTest::class
)

class ActivityTestSuite