package com.jdevs.timeo.ui.activitydetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.jdevs.timeo.R
import com.jdevs.timeo.data.FakeActivitiesRepository
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.testAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class ActivityDetailFragmentTest {

    @Inject
    lateinit var repository: FakeActivitiesRepository

    init {

        testAppComponent.inject(this)
    }

    @Before
    fun setup() {

        repository.reset()
    }

    @Test
    fun showTaskDetails_displayedInUi() = runBlockingTest {

        // GIVEN - Add an activity to the database
        val activity = Activity(name = "Activity name").also { repository.addActivity(it) }

        // WHEN - The fragment is launched to display task details
        val bundle = ActivityDetailFragmentArgs(activity).toBundle()
        launchFragmentInContainer<ActivityDetailFragment>(bundle, R.style.Theme_Timeo)

        // THEN - Activity details are displayed on the screen
        // Make sure that both title and hours count are displayed correctly
        onView(withId(R.id.title)).check(matches(withText("Activity name")))
        onView(withId(R.id.total_time)).check(matches(withText("0h")))
    }
}
