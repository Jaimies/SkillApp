package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class HistoryDiffCallbackTest : StringSpec({
    val diffCallback = HistoryDiffCallback()

    "areItemsTheSame() compares Records by id" {
        diffCallback.areItemsTheSame(createRecord(1), createRecord(1)) shouldBe true
        diffCallback.areItemsTheSame(createRecord(1), createRecord(2)) shouldBe false
    }

    "areItemsTheSame() compares Separators by date" {
        diffCallback.areItemsTheSame(
            Separator(LocalDate.ofEpochDay(1), Duration.ZERO),
            Separator(LocalDate.ofEpochDay(1), Duration.ZERO)
        ) shouldBe true

        diffCallback.areItemsTheSame(
            Separator(LocalDate.ofEpochDay(1), Duration.ZERO),
            Separator(LocalDate.ofEpochDay(2), Duration.ZERO)
        ) shouldBe false
    }

    "areItemsTheSame() returns false if the items are of different types" {
        diffCallback.areItemsTheSame(
            Separator(LocalDate.ofEpochDay(1), Duration.ZERO),
            createRecord(1)
        ) shouldBe false
    }

    "areContentsTheSame() compares by value" {
        diffCallback.areContentsTheSame(
            createRecord(1, "Name"),
            createRecord(1, "New name")
        ) shouldBe false
    }
})

fun createRecord(id: Int, name: String = ""): Record {
    return Record(id, name, Duration.ZERO, LocalDate.ofEpochDay(0))
}
