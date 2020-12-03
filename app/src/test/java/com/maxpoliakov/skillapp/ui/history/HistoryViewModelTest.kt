package com.maxpoliakov.skillapp.ui.history

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.clockOfEpochDay
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.awaitData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.LocalDate

class HistoryViewModelTest : StringSpec({
    beforeSpec {
        setClock(clockOfEpochDay(10))
    }

    "withSeparators()" {
        val pagingData = PagingData.from(
            listOf(
                Record(4, "", "", LocalDate.ofEpochDay(10)),
                Record(3, "", "", LocalDate.ofEpochDay(9)),
                Record(2, "", "", LocalDate.ofEpochDay(7))
            )
        )

        pagingData.withSeparators().awaitData() shouldBe listOf(
            Separator(LocalDate.ofEpochDay(10)),
            Record(4, "", "", LocalDate.ofEpochDay(10)),
            Separator(LocalDate.ofEpochDay(9)),
            Record(3, "", "", LocalDate.ofEpochDay(9)),
            Separator(LocalDate.ofEpochDay(7)),
            Record(2, "", "", LocalDate.ofEpochDay(7))
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})
