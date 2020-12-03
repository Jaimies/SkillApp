package com.maxpoliakov.skillapp.domain.usecase

import StubRecordsRepository
import StubStatsRepository
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class ChangeRecordTimeUseCaseTest : StringSpec({
    "updates the record and adds statistic record where time = difference between the old time and the new time" {
        val record = Record("", 1, Duration.ofHours(1), 1, LocalDate.ofEpochDay(0))
        val recordsRepository = StubRecordsRepository(listOf(record))
        val statsRepository = StubStatsRepository()
        val useCase = ChangeRecordTimeUseCase(recordsRepository, statsRepository)

        useCase.run(1, Duration.ofHours(5))

        recordsRepository.getRecord(record.id) shouldBe record.copy(time = Duration.ofHours(5))
        statsRepository.stats shouldBe listOf(
            Statistic(LocalDate.ofEpochDay(0), Duration.ofHours(4))
        )
    }
})
