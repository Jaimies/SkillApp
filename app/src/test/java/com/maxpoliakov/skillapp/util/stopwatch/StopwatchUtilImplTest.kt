package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.StubStopwatchPersistence
import com.maxpoliakov.skillapp.clockOfEpochSecond
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime


class StopwatchUtilImplTest : StringSpec({
    val coroutineScope = CoroutineScope(Unconfined)
    val addRecord = mock(AddRecordUseCase::class.java)
    val notificationUtil = mock(NotificationUtil::class.java)

    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterEach { clearInvocations(addRecord, notificationUtil) }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    fun createStopwatch(state: StopwatchState = Paused): StopwatchUtilImpl {
        val persistence = StubStopwatchPersistence(state)
        return StopwatchUtilImpl(persistence, addRecord, notificationUtil, coroutineScope)
    }

    "add the record properly" {
        val stopwatch = createStopwatch()
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId)
        stopwatch.state.value shouldBe Running(getZonedDateTime(), skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(skillId)
        verify(addRecord).run(Record("", skillId, Duration.ofSeconds(1)))
    }

    "records the time of the current timer when trying to start a new one" {
        val stopwatch = createStopwatch()
        stopwatch.toggle(skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(otherSkillId)
        verify(addRecord).run(Record("", skillId, Duration.ofSeconds(1)))
        stopwatch.state.value shouldBe Running(getZonedDateTime(), otherSkillId)
    }

    "gets the initial state from the persistence" {
        val date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
        val stopwatch = createStopwatch(Running(date, skillId))
        stopwatch.state.value shouldBe Running(date, skillId)
    }

    "persists the state and shows the notification" {
        val persistence = mock(StopwatchPersistence::class.java)
        val stopwatch = StopwatchUtilImpl(persistence, addRecord, notificationUtil, coroutineScope)
        stopwatch.toggle(skillId)
        verify(persistence).saveState(Running(getZonedDateTime(), skillId))
        verify(notificationUtil).showStopwatchNotification(skillId)
    }

    "stop() does nothing if the timer is not running" {
        val stopwatch = createStopwatch()
        stopwatch.stop()
        stopwatch.state.value shouldBe Paused
    }

    "stop() stops the timer and removes the notification" {
        val stopwatch = createStopwatch(Running(getZonedDateTime(), skillId))
        setClock(clockOfEpochSecond(1))
        stopwatch.stop()
        stopwatch.state.value shouldBe Paused
        verify(addRecord).run(Record("", skillId, Duration.ofSeconds(1)))
        verify(notificationUtil).removeStopwatchNotification()
    }
}) {
    companion object {
        private const val skillId = 12
        private const val otherSkillId = 13
    }
}
