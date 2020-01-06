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
import com.jdevs.timeo.R
import com.jdevs.timeo.data.FakeAuthRepository
import com.jdevs.timeo.data.FakeRecordsRepository
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.testAppComponent
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
    lateinit var repository: FakeRecordsRepository

    @Inject
    lateinit var fakeAuthRepository: FakeAuthRepository

    init {

        testAppComponent.inject(this)
        fakeAuthRepository.signOut()
    }

    @Before
    fun setup() {

        repository.reset()
    }

    @Test
    fun clickActivity_goToDetailFragment() = runBlockingTest {

        val activity = Activity(name = "Activity name").also { repository.addActivity(it) }

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
        verify(navController).navigate(
            ActivitiesFragmentDirections.actionActivitiesFragmentToActivityDetailsFragment(activity)
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
        verify(navController).navigate(R.id.action_activitiesFragment_to_addEditActivityFragment)
    }
}
