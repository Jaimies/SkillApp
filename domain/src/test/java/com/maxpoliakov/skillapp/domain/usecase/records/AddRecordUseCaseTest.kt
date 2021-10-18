package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration

class AddRecordUseCaseTest : StringSpec({
    val record = Record("name", 1, Duration.ofMinutes(5))
    val skill = Skill("name", Duration.ofMinutes(10), Duration.ofMinutes(5))

    "adds record if skill exists" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<StatsRepository>(relaxed = true)

        coEvery { skillRepository.getSkillById(any()) } returns skill

        val addRecordUseCase = AddRecordUseCase(recordRepository, skillRepository, statsRepository)
        addRecordUseCase.run(record)

        coVerify { recordRepository.addRecord(record) }
        coVerify { skillRepository.increaseTime(record.skillId, record.time) }
        coVerify { statsRepository.addRecord(record) }
    }

    "does not add record if skill does not exist" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val recordRepository = mockk<RecordsRepository>(relaxed = true)
        val statsRepository = mockk<StatsRepository>(relaxed = true)

        coEvery { skillRepository.getSkillById(any()) } returns null

        val addRecordUseCase = AddRecordUseCase(recordRepository, skillRepository, statsRepository)
        addRecordUseCase.run(record)

        verify { listOf(recordRepository, statsRepository) wasNot Called }
        coVerify(exactly = 0) { skillRepository.increaseTime(any(), any()) }
    }
})
