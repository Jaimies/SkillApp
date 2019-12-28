package com.jdevs.timeo.ui.activities

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jdevs.timeo.R
import com.jdevs.timeo.TimeoTestApplication
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.di.TestAppComponent
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
    lateinit var repository: TimeoRepository

    @Before
    fun setup() {

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as TimeoTestApplication

        val appComponent = app.appComponent as TestAppComponent
        appComponent.inject(this)
    }

    @Test
    fun clickActivity_goToDetailFragment() = runBlockingTest {

        val activity = Activity(name = "Activity name").also { repository.addActivity(it) }

        // GIVEN - On the activities list screen
        val scenario = launchFragmentInContainer<ActivitiesFragment>(Bundle(), R.style.Theme_Timeo)

        val navController = mock(NavController::class.java)

        scenario.onFragment {

            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first item
        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // THEN - Verify that we navigate to the first detail screen
        verify(navController).navigate(
            ActivitiesFragmentDirections.actionActivitiesFragmentToActivityDetailsFragment(activity)
        )
    }
}