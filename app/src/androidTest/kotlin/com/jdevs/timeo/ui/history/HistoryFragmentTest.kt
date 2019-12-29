package com.jdevs.timeo.ui.history

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.jdevs.timeo.R
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.FakeAuthRepository
import com.jdevs.timeo.data.source.FakeTimeoRepository
import com.jdevs.timeo.testAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {

    @Inject
    lateinit var repository: FakeTimeoRepository

    @Inject
    lateinit var authRepository: FakeAuthRepository

    init {

        testAppComponent.inject(this)
    }

    @Before
    fun setup() {

        repository.reset()
        authRepository.signOut()
    }

    @Test
    fun showRecords_clickOnItem_showsDeleteDialog() = runBlockingTest {

        // GIVEN - Add a record to the database
        repository.addRecord(Record("Activity 1"))

        // WHEN - Long click on the first item
        launchFragmentInContainer<HistoryFragment>(Bundle(), R.style.Theme_Timeo)

        onView(withId(R.id.recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, longClick())
        )

        // THEN - Make sure the dialog is shown
        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withText(R.string.yes)))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withText(R.string.no)))
            .check(matches(isDisplayed()))

        // Click on the yes button
        onView(withId(android.R.id.button1)).perform(click())

        // Make sure the record was deleted
        onView(withId(R.id.empty_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showRecords_hasNoRecords_displaysEmptyListLayout() {

        // GIVEN - No records in database

        // WHEN - Fragment is launched to display list of records
        launchFragmentInContainer<HistoryFragment>(Bundle(), R.style.Theme_Timeo)

        // THEN - No records TextView is displayed and RecyclerView is not displayed
        onView(withId(R.id.empty_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())))
    }
}
