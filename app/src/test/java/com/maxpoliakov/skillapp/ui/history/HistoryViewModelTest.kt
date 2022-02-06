package com.maxpoliakov.skillapp.ui.history

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.test.clockOfEpochDay
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetTotalTimeAtDayUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.any
import com.maxpoliakov.skillapp.test.await
import com.maxpoliakov.skillapp.test.awaitData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

class HistoryViewModelTest : StringSpec({
    beforeSpec {
        setClock(clockOfEpochDay(10))
    }

    val pagingData = PagingData.from(
        listOf(
            Record("", 4, Duration.ZERO, 0, LocalDate.ofEpochDay(10)),
            Record("", 3, Duration.ZERO, 1, LocalDate.ofEpochDay(9)),
            Record("", 2, Duration.ZERO, 2, LocalDate.ofEpochDay(7)),
            Record("", 2, Duration.ZERO, 3, LocalDate.ofEpochDay(7)),
        )
    )

    val dailyTime = Duration.ofMinutes(150)

    "records maps the input records and adds the separators where needed" {
        val getRecords = mock(GetRecordsUseCase::class.java)
        `when`(getRecords.run()).thenReturn(flowOf(pagingData))
        val getTimeAtDay = mock(GetTotalTimeAtDayUseCase::class.java)
        `when`(getTimeAtDay.run(any())).thenReturn(dailyTime)
        val viewModel = HistoryViewModel(getRecords, getTimeAtDay)

        viewModel.records.await().awaitData() shouldBe listOf(
            Separator(LocalDate.ofEpochDay(10), dailyTime),
            createUiRecord(0, LocalDate.ofEpochDay(10)),
            Separator(LocalDate.ofEpochDay(9), dailyTime),
            createUiRecord(1, LocalDate.ofEpochDay(9)),
            Separator(LocalDate.ofEpochDay(7), dailyTime),
            createUiRecord(2, LocalDate.ofEpochDay(7)),
            createUiRecord(3, LocalDate.ofEpochDay(7)),
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})

private fun createUiRecord(id: Int, date: LocalDate): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, "", Duration.ZERO, date)
}
