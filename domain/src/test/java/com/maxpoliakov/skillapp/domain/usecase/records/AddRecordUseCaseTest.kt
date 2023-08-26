package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.stopwatch.StubDateProvider
import io.kotest.core.spec.style.StringSpec
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration
import java.time.LocalDate

class AddRecordUseCaseTest : StringSpec({
    val record = Record("name", 1, Duration.ofMinutes(5).toMillis(), MeasurementUnit.Millis, date = LocalDate.EPOCH)
    val skill = Skill("name", MeasurementUnit.Millis, Duration.ofMinutes(10).toMillis(), Duration.ofMinutes(5).toMillis(), goal = null)

    "adds record if skill exists" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val dateProvider = StubDateProvider(LocalDate.EPOCH)

        coEvery { skillRepository.getSkillById(any()) } returns skill

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, dateProvider)
        addRecordUseCase.run(record)

        coVerify { recordRepository.addRecord(record) }
        coVerify { skillRepository.increaseCount(record.skillId, record.count) }
        coVerify { statsRepository.addRecord(record) }
    }

    "does not add record if skill does not exist" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val dateProvider = StubDateProvider(LocalDate.EPOCH)

        coEvery { skillRepository.getSkillById(any()) } returns null

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, dateProvider)
        addRecordUseCase.run(record)

        verify { listOf(recordRepository, statsRepository) wasNot Called }
        coVerify(exactly = 0) { skillRepository.increaseCount(any(), any()) }
    }

    "adds record with date returned from DateProvider.getCurrentDateWithRespectToDayStartTime()" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<SkillStatsRepository>(relaxed = true)
        val dateProvider = StubDateProvider(LocalDate.EPOCH.plusDays(5))

        coEvery { skillRepository.getSkillById(any()) } returns skill

        val addRecordUseCase = AddRecordUseCaseImpl(recordRepository, skillRepository, statsRepository, dateProvider)
        addRecordUseCase.run(record)

        coVerify { recordRepository.addRecord(record.copy(date = LocalDate.EPOCH.plusDays(5))) }
        coVerify { skillRepository.increaseCount(record.skillId, record.count) }
        coVerify { statsRepository.addRecord(record.copy(date = LocalDate.EPOCH.plusDays(5))) }
    }
})
