import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordDateUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class ChangeRecordDateUseCaseTest : StringSpec({
    "calls the updateRecord() on RecordsRepository, removes the record with old date from stats and adds a new one" {
        val record = Record("", 1, Duration.ofHours(1), 1, LocalDate.ofEpochDay(0))
        val recordsRepository = StubRecordsRepository(listOf(record))
        val statsRepository = StubStatsRepository()

        val useCase = ChangeRecordDateUseCase(recordsRepository, statsRepository)
        useCase.run(record.id, LocalDate.ofEpochDay(1))
        val newRecord = record.copy(date = LocalDate.ofEpochDay(1))

        recordsRepository.getRecord(record.id) shouldBe newRecord
        statsRepository.stats shouldBe listOf(
            Statistic(time = Duration.ofHours(-1), date = LocalDate.ofEpochDay(0)),
            Statistic(time = Duration.ofHours(1), date = LocalDate.ofEpochDay(1))
        )
    }
})
