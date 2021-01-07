package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.StubSharedPreferences
import com.maxpoliakov.skillapp.clockOfEpochSecond
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class StopwatchPersistenceImplTest : StringSpec({
    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    "getState() returns StopwatchPersistenceState if the data is present" {
        val persistence = createPersistence(skillId, date.toString())
        persistence.getState() shouldBe Running(date, skillId)
    }

    "getState() returns null if the skill id is not defined" {
        val persistence = createPersistence(-1, date.toString())
        persistence.getState() shouldBe Paused
    }

    "getState() returns null if the start time is not defined" {
        val persistence = createPersistence(skillId, "")
        persistence.getState() shouldBe Paused
    }
    
    "saveState() saves StopwatchState.Running" {
        val persistence = createPersistence(-1, "")
        val state = Running(date, skillId)
        persistence.saveState(state)
        persistence.getState() shouldBe state
    }

    "saveState() saves StopwatchState.Paused" {
        val persistence = createPersistence(-1, "")
        persistence.saveState(Paused)
        persistence.getState() shouldBe Paused
    }
}) {
    companion object {
        private const val skillId = 12
        private val date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault())

        private fun createPersistence(skillId: Int, startTime: String): StopwatchPersistence {
            val prefs = StubSharedPreferences(
                mapOf(
                    "STOPWATCH_SKILL_ID" to skillId,
                    "STOPWATCH_START_TIME" to startTime
                )
            )
            return StopwatchPersistenceImpl(prefs)
        }
    }
}