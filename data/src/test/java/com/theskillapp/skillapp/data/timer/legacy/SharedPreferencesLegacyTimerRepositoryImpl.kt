package com.theskillapp.skillapp.data.timer.legacy

import com.theskillapp.skillapp.data.StubSharedPreferences
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.repository.LegacyTimerRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class SharedPreferencesLegacyTimerRepositoryImpl : StringSpec({
    "getState() returns state with a timer if data present" {
        val persistence = createPersistence(skillId, date.toString(), groupId)
        persistence.getTimer() shouldBe Timer(skillId, date)
    }

    "getState() returns state without timers, if skill id is not defined" {
        val persistence = createPersistence(-1, date.toString(), groupId)
        persistence.getTimer() shouldBe null
    }

    "getState() returns state without timers if startTime is not defined" {
        val persistence = createPersistence(skillId, "", groupId)
        persistence.getTimer() shouldBe null
    }

    "getState() returns state without timers if startTime is malformed" {
        val persistence = createPersistence(skillId, "malformed", groupId)
        persistence.getTimer() shouldBe null
    }
}) {
    companion object {
        private const val skillId = 12
        private const val groupId = 5
        private val date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault())

        private fun createPersistence(skillId: Int, startTime: String, groupId: Int): LegacyTimerRepository {
            val prefs = StubSharedPreferences(
                mapOf(
                    "STOPWATCH_SKILL_ID" to skillId,
                    "STOPWATCH_START_TIME" to startTime,
                    "STOPWATCH_GROUP_ID" to groupId,
                )
            )
            return SharedPreferencesLegacyTimerRepository(prefs)
        }
    }
}
