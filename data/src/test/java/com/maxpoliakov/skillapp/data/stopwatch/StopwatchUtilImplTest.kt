package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.clockOfEpochSecond
import com.maxpoliakov.skillapp.data.StubStopwatchPersistence
import com.maxpoliakov.skillapp.dateOfEpochSecond
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime

class StopwatchUtilImplTest : StringSpec({
    val coroutineScope = CoroutineScope(Unconfined)
    val addRecord = mockk<AddRecordUseCase>(relaxed = true)
    val notificationUtil = mockk<NotificationUtil>(relaxed = true)

    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterEach { clearAllMocks() }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    coEvery { addRecord.run(any()) }.returns(1)

    fun getRunningState() = Running(getZonedDateTime(), skillId)

    fun createStopwatch(state: StopwatchState = Paused): StopwatchUtilImpl {
        val persistence = StubStopwatchPersistence(state)
        return StopwatchUtilImpl(persistence, addRecord, notificationUtil, coroutineScope)
    }

    "add the record properly" {
        val stopwatch = createStopwatch()
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId).await()
        stopwatch.state.value shouldBe getRunningState()
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(skillId).await()

        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
    }

    "records the time of the current timer when trying to start a new one" {
        val stopwatch = createStopwatch()
        stopwatch.toggle(skillId).await()
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(otherSkillId).await()
        coVerify { addRecord.run(Record("", skillId, Duration.ofSeconds(1))) }
        stopwatch.state.value shouldBe Running(getZonedDateTime(), otherSkillId)
    }

    "gets the initial state from the persistence" {
        val date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
        val stopwatch = createStopwatch(Running(date, skillId))
        stopwatch.state.value shouldBe Running(date, skillId)
    }

    "persists the state and shows the notification" {
        val persistence = mockk<StopwatchPersistence>(relaxed = true)
        val stopwatch = StopwatchUtilImpl(persistence, addRecord, notificationUtil, coroutineScope)
        stopwatch.toggle(skillId).await()

        verify { persistence.saveState(getRunningState()) }
        verify { notificationUtil.showStopwatchNotification(getRunningState()) }
    }

    "start() does nothing if the timer is already running with the same id" {
        val stopwatch = createStopwatch()
        stopwatch.start(skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.start(skillId)
        stopwatch.state.value shouldBe Running(dateOfEpochSecond(0), skillId)
    }

    "stop() does nothing if the timer is not running" {
        val stopwatch = createStopwatch()
        stopwatch.stop().await()
        stopwatch.state.value shouldBe Paused
    }

    "stop() stops the timer and removes the notification" {
        val stopwatch = createStopwatch(getRunningState())
        setClock(clockOfEpochSecond(1))
        stopwatch.stop().await()
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
        private const val skillId = 12
        private const val otherSkillId = 13
    }
}
