package com.maxpoliakov.skillapp.screenshots

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.serialization.DBMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.test.R
import com.maxpoliakov.skillapp.ui.MainActivity
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
    lateinit var stopwatch: Stopwatch

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

        val skillData = listOf(
            SkillData(R.string.pull_ups, 2500, -1, MeasurementUnit.Times),
            SkillData(R.string.jogging, 350_000, -1, MeasurementUnit.Meters),
            SkillData(R.string.web_design, Duration.ofHours(479).toMillis(), 1, MeasurementUnit.Millis),
            SkillData(R.string.app_design, Duration.ofHours(677).toMillis(), 1, MeasurementUnit.Millis, Duration.ofHours(3).toMillis()),
        )

        val skills = skillData.map(::createSkill)

        val recordsData = listOf(
            Triple(2, 0, Duration.ofHours(1)),
            Triple(3, 0, Duration.ofMinutes(90)),
            Triple(2, 0, Duration.ofHours(1)),
            Triple(2, 0, Duration.ofHours(1)),
            Triple(3, 0, Duration.ofHours(2)),

            Triple(3, 1, Duration.ofHours(1)),
            Triple(2, 1, Duration.ofMinutes(90)),
            Triple(3, 1, Duration.ofHours(2)),
            Triple(2, 1, Duration.ofHours(2)),

            Triple(2, 2, Duration.ofHours(3)),
            Triple(3, 2, Duration.ofHours(3)),

            Triple(2, 3, Duration.ofHours(3)),
            Triple(3, 3, Duration.ofHours(4)),

            Triple(2, 4, Duration.ofHours(3)),
            Triple(3, 4, Duration.ofMinutes(150)),

            Triple(2, 5, Duration.ofHours(3)),
            Triple(3, 5, Duration.ofHours(4)),

            Triple(2, 6, Duration.ofHours(3)),
            Triple(3, 6, Duration.ofHours(3)),
        )

        val records = recordsData.map { triple ->
            Record(
                skillId = triple.first + 1,
                name = skills[triple.first].name,
                date = LocalDate.now().minusDays(triple.second.toLong()),
                count = triple.third.toMillis(),
                unit = MeasurementUnit.Millis,
            )
        }

        skills.forEach { skill ->
            db.skillDao().insert(skill)
        }

        records.forEach { record ->
            addRecordUseCase.run(record)
        }

        db.skillGroupDao().insert(DBGroup(id = 1, name = context.getString(R.string.ux_design), order = -1))

        makeScreenshot("skilllist")

        navigate(com.maxpoliakov.skillapp.R.id.backup_fragment_dest)
        makeScreenshot("backup")

        navigate(com.maxpoliakov.skillapp.R.id.statistics_fragment_dest)
        makeScreenshot("stats")

        setTheme(Theme.Dark)
        setupNavController()
        val directions = MainDirections.actionToSkillDetailFragment(4)
        navigate(directions)
        stopwatch.stop()
        makeScreenshot("skilldetail")

        val groupDirections = MainDirections.actionToSkillGroupFragment(1)
        navigate(groupDirections)
        delay(5_000)
        makeScreenshot("skillgroup")
    }

    private fun createSkill(data: SkillData): DBSkill {
        val name: String = context.getString(data.nameResId)

        return DBSkill(
            name = name,
            totalTime = data.totalCount,
            groupId = data.groupId,
            unit = data.unit.mapToUI(),
            goalType = Goal.Type.Daily,
            goalTime = data.goalCount,
        )
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
            com.maxpoliakov.skillapp.util.ui.setTheme(theme)
        }
    }

    private suspend fun makeScreenshot(name: String) {
        getInstrumentation().waitForIdleSync()
        delay(500)
        Screengrab.screenshot(name)
    }

    data class SkillData(
        @StringRes val nameResId: Int,
        val totalCount: Long,
        val groupId: Int,
        val unit: MeasurementUnit<*>,
        val goalCount: Long = 0,
    )
}
