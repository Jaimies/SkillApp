package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.test.clockOfEpochSecond
import com.maxpoliakov.skillapp.data.StubSharedPreferences
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class StopwatchPersistenceImplTest : StringSpec({
    beforeEach { setClock(clockOfEpochSecond(0)) }
    afterSpec { setClock(Clock.systemDefaultZone()) }

    "getState() returns StowpatchState.Running if the data is present" {
        val persistence = createPersistence(skillId, date.toString(), groupId)
        persistence.getState() shouldBe Running(date, skillId, groupId)
    }

    "getState() returns StopwatchState.Paused if the skill id is not defined" {
        val persistence = createPersistence(-1, date.toString(), groupId)
        persistence.getState() shouldBe Paused
    }

    "getState() returns StopwatchState.Paused if the start time is not defined" {
        val persistence = createPersistence(skillId, "", groupId)
        persistence.getState() shouldBe Paused
    }

    "getState() returns StopwatchState.Paused if the start time is not a valid date" {
        val persistence = createPersistence(skillId, "malformed", groupId)
        persistence.getState() shouldBe Paused
    }

    "saveState() saves StopwatchState.Running" {
        val persistence = createPersistence(-1, "", groupId)
        val state = Running(date, skillId, groupId)
        persistence.saveState(state)
        persistence.getState() shouldBe state
    }

    "saveState() saves StopwatchState.Paused" {
        val persistence = createPersistence(-1, "", groupId)
        persistence.saveState(Paused)
        persistence.getState() shouldBe Paused
    }
}) {
    companion object {
        private const val skillId = 12
        private const val groupId = 5
        private val date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault())

        private fun createPersistence(skillId: Int, startTime: String, groupId: Int): StopwatchRepository {
            val prefs = StubSharedPreferences(
                mapOf(
                    "STOPWATCH_SKILL_ID" to skillId,
                    "STOPWATCH_START_TIME" to startTime,
                    "STOPWATCH_GROUP_ID" to groupId,
                )
            )
            return StopwatchRepositoryImpl(prefs)
        }
    }
}
