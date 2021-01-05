package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.StubStopwatchPersistence
import com.maxpoliakov.skillapp.clockOfEpochSecond
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime


class StopwatchUtilImplTest : StringSpec({
    val callback = mock({ _: Duration -> }::class.java)

    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    "ticks the time properly" {
        val persistence = StubStopwatchPersistence(Paused)
        val stopwatch = StopwatchUtilImpl(persistence)
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId, callback)
        stopwatch.state.value shouldBe Running(getZonedDateTime(), skillId)
        setClock(clockOfEpochSecond(1))
        stopwatch.toggle(skillId, callback)
        verify(callback).invoke(Duration.ofSeconds(1))
    }

    "gets the initial state from the persistence" {
        val date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
        val persistence = StubStopwatchPersistence(Running(date, skillId))
        val stopwatch =StopwatchUtilImpl(persistence)
        stopwatch.state.value shouldBe Running(date, skillId)
    }

    "persists the state" {
        val persistence = mock(StopwatchPersistence::class.java)
        val stopwatch = StopwatchUtilImpl(persistence)
        stopwatch.toggle(skillId, callback)
        verify(persistence).saveState(Running(getZonedDateTime(), skillId))
    }
}) {
    companion object {
        private const val skillId = 12
    }
}
