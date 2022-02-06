package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.test.clockOfEpochSecond
import com.maxpoliakov.skillapp.data.StubStopwatchPersistence
import com.maxpoliakov.skillapp.test.dateOfEpochSecond
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime

class StubSkillRepository : SkillRepository {
    override fun getSkills() = flowOf<List<Skill>>()
    override fun getSkillFlowById(id: Id) = flowOf<Skill>()
    override fun getTopSkills(count: Int) = flowOf<List<Skill>>()

    override suspend fun getSkillById(id: Id): Skill? {
        if (id == StopwatchUtilImplTest.skillId || id == StopwatchUtilImplTest.otherSkillId)
            return Skill("", Duration.ZERO, Duration.ZERO, groupId = StopwatchUtilImplTest.groupId)

        return null
    }

    override suspend fun addSkill(skill: Skill) = 1L

    override suspend fun updateName(skillId: Int, newName: String) {}
    override suspend fun updateGoal(skillId: Int, newGoal: Goal?) {}
    override suspend fun deleteSkill(skill: Skill) {}
    override suspend fun updateOrder(skillId: Int, newOrder: Int) {}
    override suspend fun increaseTime(id: Id, time: Duration) {}
    override suspend fun decreaseTime(id: Id, time: Duration) {}
}

class StopwatchUtilImplTest : StringSpec({
    val addRecord = mockk<AddRecordUseCase>(relaxed = true)
    val notificationUtil = mockk<NotificationUtil>(relaxed = true)
    val skillRepository = StubSkillRepository()

    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterEach { clearAllMocks() }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    coEvery { addRecord.run(any()) }.returns(1)

    fun getRunningState() = Running(getZonedDateTime(), skillId, groupId)

    fun createStopwatch(state: StopwatchState = Paused): StopwatchUtilImpl {
        val persistence = StubStopwatchPersistence(state)
        return StopwatchUtilImpl(persistence, addRecord, skillRepository, notificationUtil)
    }

    "add the record properly" {
        val stopwatch = createStopwatch()
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId)
        stopwatch.state.value shouldBe getRunningState()
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(skillId)

        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
    }

    "records the time of the current timer when trying to start a new one" {
        val stopwatch = createStopwatch()
        stopwatch.toggle(skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(otherSkillId)
        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
        stopwatch.state.value shouldBe Running(getZonedDateTime(), otherSkillId, groupId)
    }

    "gets the initial state from the persistence" {
        val date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
        val stopwatch = createStopwatch(Running(date, skillId, groupId))
        stopwatch.state.value shouldBe Running(date, skillId, groupId)
    }

    "persists the state and shows the notification" {
        val persistence = mockk<StopwatchPersistence>(relaxed = true)
        val stopwatch = StopwatchUtilImpl(persistence, addRecord, skillRepository, notificationUtil)
        stopwatch.toggle(skillId)

        verify { persistence.saveState(getRunningState()) }
        verify { notificationUtil.showStopwatchNotification(getRunningState()) }
    }

    "start() does nothing if the timer is already running with the same id" {
        val stopwatch = createStopwatch()
        stopwatch.start(skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.start(skillId)
        stopwatch.state.value shouldBe Running(dateOfEpochSecond(0), skillId, groupId)
    }

    "start() stops the existing timer and starts a new one" {
        val stopwatch = createStopwatch()
        stopwatch.start(skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.start(otherSkillId)
        setClock(clockOfEpochSecond(2))
        stopwatch.state.value shouldBe Running(dateOfEpochSecond(1), otherSkillId, groupId)
        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
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
        setClock(clockOfEpochSecond(1))
        stopwatch.stop()
        stopwatch.state.value shouldBe Paused
        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
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
}) {
    companion object {
        const val skillId = 12
        const val otherSkillId = 13
        const val groupId = 5
    }
}
