package com.jdevs.timeo.ui.history

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.jdevs.timeo.R
import com.jdevs.timeo.data.FakeAuthRepository
import com.jdevs.timeo.data.FakeRecordsRepository
import com.jdevs.timeo.testAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var repository: FakeRecordsRepository

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
    fun showRecords_hasNoRecords_displaysEmptyListLayout() {

        // GIVEN - No records in database

        // WHEN - Fragment is launched to display list of records
        launchFragmentInContainer<HistoryFragment>(Bundle(), R.style.Theme_Timeo)

        // THEN - No records TextView is displayed and RecyclerView is not displayed
        onView(withId(R.id.empty_list_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())))
    }
}
