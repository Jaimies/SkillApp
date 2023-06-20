package com.maxpoliakov.skillapp.data.timer.legacy

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.stopwatch.StubTimerRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime

class LegacyTimerRepositoryMigratorTest : StringSpec({
    "adds timer to target and deletes timer from legacy timer repo if target has no timers and legacy repo has a timer" {
        val timerRepository = StubTimerRepository(listOf())
        val legacyTimerRepository = StubLegacyTimerRepository(timer)
        val timerMigrator = LegacyTimerRepositoryMigratorImpl(legacyTimerRepository)
        timerMigrator.migrate(timerRepository)
        timerRepository.getAll().first() shouldBe listOf(timer)
        legacyTimerRepository.getTimer() shouldBe null
    }

    "does nothing if legacy repo does not have a timer" {
        val timerRepository = StubTimerRepository(listOf())
        val legacyTimerRepository = StubLegacyTimerRepository(null)
        val timerMigrator = LegacyTimerRepositoryMigratorImpl(legacyTimerRepository)
        timerMigrator.migrate(timerRepository)
        timerRepository.getAll().first() shouldBe listOf()
        legacyTimerRepository.getTimer() shouldBe null
    }

    "deletes timer from legacy repo if target already has a timer" {
        val timerRepository = StubTimerRepository(listOf(timer))
        val legacyTimerRepository = StubLegacyTimerRepository(otherTimer)
        val timerMigrator = LegacyTimerRepositoryMigratorImpl(legacyTimerRepository)
        timerMigrator.migrate(timerRepository)
        timerRepository.getAll().first() shouldBe listOf(timer)
        legacyTimerRepository.getTimer() shouldBe null
    }
}) {
    companion object {
        private val timer = Timer(2, ZonedDateTime.now())
        private val otherTimer = Timer(3, ZonedDateTime.now())
    }
}
