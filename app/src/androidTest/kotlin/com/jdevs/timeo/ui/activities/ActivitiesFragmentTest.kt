package com.jdevs.timeo.ui.activities

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.jdevs.timeo.OverviewDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.data.FakeActivitiesRepository
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.testAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class ActivitiesFragmentTest {

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
    fun clickActivity_goToDetailFragment() = runBlockingTest {

        repository.addActivity("Activity name")

        // GIVEN - On the activities list screen
        val scenario = launchFragmentInContainer<ActivitiesFragment>(Bundle(), R.style.Theme_Timeo)

        val navController = mock(NavController::class.java)

        scenario.onFragment {

            Navigation.setViewNavController(it.requireView(), navController)
        }

        // WHEN - Click on the first item
        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // THEN - Verify that we navigate to the first detail screen

        val activity = repository.getActivityById("0").value!!.mapToPresentation()
        verify(navController).navigateAnimated(
            OverviewDirections.actionToActivityDetailFragment(activity)
        )
    }

    @Test
    fun noActivities_clickOnAddActivityButton_navigateToAddActivityFragment() {

        // GIVEN - On the activities list screen with no activities
        val scenario = launchFragmentInContainer<ActivitiesFragment>(Bundle(), R.style.Theme_Timeo)

        val navController = mock(NavController::class.java)

        scenario.onFragment {

            Navigation.setViewNavController(it.requireView(), navController)
        }

        // WHEN - Click on the Create Activity button
        onView(withId(R.id.empty_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.create_activity_button)).perform(click())

        // THEN - Verify that we navigate to AddEditActivityFragment
        verify(navController).navigateAnimated(R.id.addactivity_fragment_dest)
    }
}
