package com.maxpoliakov.skillapp.screenshots

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.test.R
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    lateinit var stopwatchUtil: StopwatchUtil

    @Inject
    lateinit var addRecordUseCase: AddRecordUseCase

    private lateinit var navController: NavController

    private val context = getInstrumentation().context

    @Before
    fun init() {
        hiltRule.inject()
        setupNavController()
        CleanStatusBar.enableWithDefaults()
        com.maxpoliakov.skillapp.util.ui.setTheme(Theme.Light)
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
            R.string.playing_guitar to 400,
            R.string.cycling to 500,
            R.string.ux_design to 1000,
            R.string.software_engineering to 2500,
        )

        val skills = skillData.map {
            createSkill(it)
        }

        val recordsData = listOf(
            Triple(3, 0, Duration.ofHours(1)),
            Triple(2, 0, Duration.ofMinutes(90)),
            Triple(3, 0, Duration.ofHours(1)),
            Triple(3, 0, Duration.ofHours(1)),
            Triple(2, 0, Duration.ofHours(2)),

            Triple(2, 1, Duration.ofHours(1)),
            Triple(3, 1, Duration.ofMinutes(90)),
            Triple(2, 1, Duration.ofHours(2)),
            Triple(3, 1, Duration.ofHours(2)),

            Triple(3, 2, Duration.ofHours(3)),
            Triple(2, 2, Duration.ofHours(3)),

            Triple(3, 3, Duration.ofHours(3)),
            Triple(2, 3, Duration.ofHours(4)),

            Triple(3, 4, Duration.ofHours(3)),
            Triple(2, 4, Duration.ofMinutes(150)),

            Triple(3, 5, Duration.ofHours(3)),
            Triple(2, 5, Duration.ofHours(4)),

            Triple(3, 6, Duration.ofHours(3)),
            Triple(2, 6, Duration.ofHours(3)),
        )

        val records = recordsData.map { triple ->
            Record(
                skillId = triple.first + 1,
                name = skills[triple.first].name,
                date = LocalDate.now().minusDays(triple.second.toLong()),
                time = triple.third
            )
        }

        skills.forEach { skill ->
            db.skillDao().insert(skill)
        }

        records.forEach { record ->
            addRecordUseCase.run(record)
        }
        stopwatchUtil.start(3)

        makeScreenshot("skilllist")

        navigate(com.maxpoliakov.skillapp.R.id.history_fragment_dest)
        makeScreenshot("history")

        navigate(com.maxpoliakov.skillapp.R.id.statistics_fragment_dest)
        setTheme(Theme.Dark)
        makeScreenshot("stats")

        setupNavController()
        val directions = MainDirections.actionToSkillDetailFragment(3)
        navigate(directions)
        makeScreenshot("skilldetail")
    }

    private fun createSkill(pair: Pair<Int, Int>): DBSkill {
        val name: String = context.getString(pair.first)
        val totalTime: Duration = Duration.ofHours(pair.second.toLong())
        return DBSkill(name = name, totalTime = totalTime)
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
}
