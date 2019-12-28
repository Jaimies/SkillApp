package com.jdevs.timeo.ui.profile

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.jdevs.timeo.R
import com.jdevs.timeo.data.source.FakeUserManager
import com.jdevs.timeo.testAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    @Inject
    lateinit var fakeUserManager: FakeUserManager

    init {

        testAppComponent.inject(this)
    }

    @Test
    fun showProfile_clickSignIn_navigateToSignInFragment() {

        // GIVEN - The user is not signed in
        fakeUserManager.signOut()

        // WHEN - The fragment is launched to show profile
        val scenario = launchFragmentInContainer<ProfileFragment>(Bundle(), R.style.Theme_Timeo)

        val navController = mock(NavController::class.java)

        scenario.onFragment {

            Navigation.setViewNavController(it.requireView(), navController)
        }

        // THEN - The sign out button is not displayed
        onView(withId(R.id.sign_out_btn)).check(matches(not(isDisplayed())))

        // The sign in button is displayed and clicked
        onView(withId(R.id.sign_in_btn))
            .check(matches(isDisplayed()))
            .perform(click())

        // Verify we navigate to SignInFragment
        verify(navController).navigate(R.id.action_profileFragment_to_signInFragment)
    }

    @Test
    fun showProfile_clickSignOut_popBackStack() {

        // GIVEN - The user is signed in
        fakeUserManager.signIn()

        // WHEN - The fragment is launched to show profile
        val scenario = launchFragmentInContainer<ProfileFragment>(Bundle(), R.style.Theme_Timeo)

        val navController = mock(NavController::class.java)

        scenario.onFragment {

            Navigation.setViewNavController(it.requireView(), navController)
        }

        // THEN - The sign out button is displayed and the sign in button is not displayed
        onView(withId(R.id.sign_out_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.sign_in_btn)).check(matches(not(isDisplayed())))
    }
}
