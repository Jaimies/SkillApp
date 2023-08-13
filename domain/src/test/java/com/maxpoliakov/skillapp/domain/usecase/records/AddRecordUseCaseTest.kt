package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.stopwatch.StubUserPreferenceRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.time.MutableClock
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

class AddRecordUseCaseTest : StringSpec({
    val record = Record("name", 1, Duration.ofMinutes(5).toMillis(), MeasurementUnit.Millis, date = LocalDate.EPOCH)
    val skill = Skill("name", MeasurementUnit.Millis, Duration.ofMinutes(10).toMillis(), Duration.ofMinutes(5).toMillis(), goal = null)
    val clock = MutableClock(Instant.EPOCH, ZoneOffset.UTC)

    "adds record if skill exists" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val preferenceRepository = StubUserPreferenceRepository()

        coEvery { skillRepository.getSkillById(any()) } returns skill

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, preferenceRepository, clock)
        addRecordUseCase.run(record)

        coVerify { recordRepository.addRecord(record) }
        coVerify { skillRepository.increaseCount(record.skillId, record.count) }
        coVerify { statsRepository.addRecord(record) }
    }

    "does not add record if skill does not exist" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val preferenceRepository = StubUserPreferenceRepository()

        coEvery { skillRepository.getSkillById(any()) } returns null

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, preferenceRepository, clock)
        addRecordUseCase.run(record)

        verify { listOf(recordRepository, statsRepository) wasNot Called }
        coVerify(exactly = 0) { skillRepository.increaseCount(any(), any()) }
    }

    "adds record to previous day if day has not started yet" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val preferenceRepository = StubUserPreferenceRepository(LocalTime.NOON)

        coEvery { skillRepository.getSkillById(any()) } returns skill

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, preferenceRepository, clock)
        addRecordUseCase.run(record)

        coVerify { recordRepository.addRecord(record.copy(date = record.date.minusDays(1))) }
        coVerify { skillRepository.increaseCount(record.skillId, record.count) }
        coVerify { statsRepository.addRecord(record.copy(date = record.date.minusDays(1))) }
    }
})
