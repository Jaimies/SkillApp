package com.theskillapp.skillapp.screenshots

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.theskillapp.skillapp.MainDirections
import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.group.DBGroup
import com.theskillapp.skillapp.data.serialization.DBMeasurementUnit.Companion.mapToUI
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.domain.usecase.records.AddRecordUseCase
import com.theskillapp.skillapp.model.Theme
import com.theskillapp.skillapp.test.R
import com.theskillapp.skillapp.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar
import tools.fastlane.screengrab.locale.LocaleTestRule
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltAndroidTest
class ScreenshotsTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    val localeTestRule = LocaleTestRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var stopwatch: StubStopwatch

    @Inject
    lateinit var addRecordUseCase: AddRecordUseCase

    private lateinit var navController: NavController

    private val context = getInstrumentation().context

    @Before
    fun init() {
        hiltRule.inject()
        setupNavController()
        CleanStatusBar.enableWithDefaults()

        GlobalScope.launch {
            setTheme(Theme.Light)
        }
    }

    private fun setupNavController() {
        activityRule.scenario.onActivity { activity ->
            this.navController = activity.navController
        }
    }

    @After
    fun teardown() {
        CleanStatusBar.disable()
    }

    @Test
    fun makeScreenshots() = runBlocking {
        skills.forEach { skill ->
            db.skillDao().insert(skill)
        }

        records.forEach { record ->
            addRecordUseCase.run(record)
        }

        db.skillGroupDao().insert(DBGroup(id = 1, name = context.getString(R.string.ux_design), order = -1))

        stopwatch.setState(Stopwatch.State(
            timers = listOf(Timer(
                skillId = skill_webDesign.id,
                startTime = ZonedDateTime.now().minusHours(1).minusMinutes(3).minusSeconds(15)
            ))
        ))

        makeScreenshot("skilllist")

        navigate(com.theskillapp.skillapp.R.id.shared_storage_backup_fragment_dest)
        makeScreenshot("backup")

        navigate(com.theskillapp.skillapp.R.id.statistics_fragment_dest)
        makeScreenshot("stats")

        setTheme(Theme.Dark)
        setupNavController()
        val directions = MainDirections.actionToSkillDetailFragment(4)
        navigate(directions)
        stopwatch.setState(Stopwatch.State(timers = listOf()))
        makeScreenshot("skilldetail")

        val groupDirections = MainDirections.actionToSkillGroupFragment(1)
        navigate(groupDirections)
        delay(5_000)
        makeScreenshot("skillgroup")
    }

    private suspend fun navigate(@IdRes destinationId: Int) {
        withContext(Dispatchers.Main) {
            navController.navigate(destinationId)
        }
    }

    private suspend fun navigate(directions: NavDirections) {
        withContext(Dispatchers.Main) {
            navController.navigate(directions)
        }
    }

    private suspend fun setTheme(theme: Theme) {
        withContext(Dispatchers.Main) {
            com.theskillapp.skillapp.shared.extensions.setTheme(theme)
        }
    }

    private suspend fun makeScreenshot(name: String) {
        getInstrumentation().waitForIdleSync()
        delay(500)
        Screengrab.screenshot(name)
    }
}
