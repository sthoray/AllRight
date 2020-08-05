package com.sthoray.allright.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            // Set the [testCoroutineDispatcher] as an underlying dispatcher of [Dispatchers.Main]. All
            // consecutive usages of [Dispatchers.Main] will use given to this dispatcher under the hood.
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            // Reset the state of [Dispatchers.Main] to the original main dispatcher and clean up to make sure no other work is running.
            Dispatchers.resetMain()
            testCoroutineScope.cleanupTestCoroutines()
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }
}
