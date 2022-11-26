package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.data.StubStopwatchPersistence
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.test.dateOfEpochSecond
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.time.MutableClock
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class StubSkillRepository : SkillRepository {
    override fun getSkills() = flowOf<List<Skill>>()
    override fun getSkills(criteria: SkillSelectionCriteria) = getSkills()
    override fun getSkillsWithLastWeekCount(unit: MeasurementUnit) = getSkills()
    override fun getSkillFlowById(id: Id) = flowOf<Skill>()
    override fun getTopSkills(count: Int) = flowOf<List<Skill>>()

    override suspend fun getSkillById(id: Id): Skill? {
        if (id == StopwatchUtilImplTest.skillId || id == StopwatchUtilImplTest.otherSkillId)
            return Skill("", MeasurementUnit.Millis, 0, 0, groupId = StopwatchUtilImplTest.groupId)

        return null
    }

    override suspend fun addSkill(skill: Skill) = 1L

    override suspend fun updateName(skillId: Int, newName: String) {}
    override suspend fun updateGoal(skillId: Int, newGoal: Goal?) {}
    override suspend fun deleteSkill(skill: Skill) {}
    override suspend fun updateOrder(skillId: Int, newOrder: Int) {}
    override suspend fun increaseCount(id: Id, count: Long) {}
    override suspend fun decreaseCount(id: Id, count: Long) {}
}

class StopwatchUtilImplTest : StringSpec({
    val addRecord = mockk<AddRecordUseCase>(relaxed = true)
    val notificationUtil = mockk<NotificationUtil>(relaxed = true)
    val skillRepository = StubSkillRepository()
    var clock = MutableClock(Instant.EPOCH, UTC)

    beforeEach { clock = MutableClock(Instant.EPOCH, UTC) }
    afterEach { clearAllMocks() }

    coEvery { addRecord.run(any()) }.returns(1)

    fun getRunningState() = Running(ZonedDateTime.now(clock), skillId, groupId)

    fun createStopwatch(state: StopwatchState = Paused): StopwatchUtilImpl {
        val persistence = StubStopwatchPersistence(state)
        return StopwatchUtilImpl(persistence, addRecord, skillRepository, notificationUtil, clock)
    }

    fun createRecord(
        duration: Duration = Duration.ofSeconds(1),
        date: LocalDate = LocalDate.ofEpochDay(0),
        dateTimeRange: ClosedRange<LocalDateTime> =
            LocalDateTime.ofEpochSecond(0, 0, UTC)
                .rangeTo(LocalDateTime.ofEpochSecond(1, 0, UTC))
    ) = Record(
        name = "",
        skillId = skillId,
        count = duration.toMillis(),
        unit = MeasurementUnit.Millis,
        date = date,
        dateTimeRange = dateTimeRange,
    )

    "add the record properly" {
        val stopwatch = createStopwatch()
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId)
        stopwatch.state.value shouldBe getRunningState()
        clock.withInstant(Instant.ofEpochSecond(1))
        stopwatch.toggle(skillId)

        coVerify { addRecord.run(createRecord()) }
    }

    "records the time of the current timer when trying to start a new one" {
        val stopwatch = createStopwatch()
        stopwatch.toggle(skillId)
        clock.withInstant(Instant.ofEpochSecond(1))
        stopwatch.toggle(otherSkillId)
        coVerify { addRecord.run(createRecord()) }
        stopwatch.state.value shouldBe Running(ZonedDateTime.now(clock), otherSkillId, groupId)
    }

    "gets the initial state from the persistence" {
        val date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
        val stopwatch = createStopwatch(Running(date, skillId, groupId))
        stopwatch.state.value shouldBe Running(date, skillId, groupId)
    }

    "persists the state and shows the notification" {
        val persistence = mockk<StopwatchPersistence>(relaxed = true)
        val stopwatch = StopwatchUtilImpl(persistence, addRecord, skillRepository, notificationUtil, clock)
        stopwatch.toggle(skillId)

        verify { persistence.saveState(getRunningState()) }
        verify { notificationUtil.showStopwatchNotification(getRunningState()) }
    }

    "start() does nothing if the timer is already running with the same id" {
        val stopwatch = createStopwatch()
        stopwatch.start(skillId)
        clock.withInstant(Instant.ofEpochSecond(1))
        stopwatch.start(skillId)
        stopwatch.state.value shouldBe Running(dateOfEpochSecond(0), skillId, groupId)
    }

    "start() stops the existing timer and starts a new one" {
        val stopwatch = createStopwatch()
        stopwatch.start(skillId)
        clock.withInstant(Instant.ofEpochSecond(1))
        stopwatch.start(otherSkillId)
        clock.withInstant(Instant.ofEpochSecond(2))
        stopwatch.state.value shouldBe Running(dateOfEpochSecond(1), otherSkillId, groupId)
        coVerify { addRecord.run(createRecord()) }
    }

    "stop() does nothing if the timer is not running" {
        val stopwatch = createStopwatch()
        stopwatch.stop()
        stopwatch.state.value shouldBe Paused
    }

    "start() does not start the timer if the skill with given id does not exist" {
        val stopwatch = createStopwatch()
        stopwatch.start(259)
        stopwatch.state.value shouldBe Paused
    }

    "stop() stops the timer and removes the notification" {
        val stopwatch = createStopwatch(getRunningState())
        clock.withInstant(Instant.ofEpochSecond(1))
        stopwatch.stop()
        stopwatch.state.value shouldBe Paused
        coVerify { addRecord.run(createRecord()) }
        verify { notificationUtil.removeStopwatchNotification() }
    }

    "shows the notification if the state is Running on startup" {
        createStopwatch(getRunningState())
        verify { notificationUtil.showStopwatchNotification(getRunningState()) }
    }

    "removes the notification if the state isd Paused on startup" {
        createStopwatch(Paused)
        verify { notificationUtil.removeStopwatchNotification() }
    }

    "adds the record with the date of stopwatch start" {
        val stopwatchUtil = createStopwatch(getRunningState())
        clock.withInstant(Instant.EPOCH.plus(2, ChronoUnit.DAYS))
        stopwatchUtil.stop()
        coVerify {
            addRecord.run(
                createRecord(
                    duration = Duration.ofDays(2),
                    dateTimeRange = LocalDate.ofEpochDay(0).atStartOfDay()..LocalDate.ofEpochDay(2).atStartOfDay()
                )
            )
        }
    }
}) {
    companion object {
        const val skillId = 12
        const val otherSkillId = 13
        const val groupId = 5
    }
}
