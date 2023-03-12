package com.maxpoliakov.skillapp.ui.history.recyclerview

import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class HistoryDiffCallbackTest : StringSpec({
    val diffCallback = HistoryDiffCallback()

    "areItemsTheSame() compares Records by id" {
        diffCallback.areItemsTheSame(createRecord(1), createRecord(1)) shouldBe true
        diffCallback.areItemsTheSame(createRecord(1), createRecord(2)) shouldBe false
    }

    "areItemsTheSame() compares Separators by date" {
        diffCallback.areItemsTheSame(
            createSeparator(LocalDate.ofEpochDay(1), 200),
            createSeparator(LocalDate.ofEpochDay(1), 100),
        ) shouldBe true

        diffCallback.areItemsTheSame(
            createSeparator(LocalDate.ofEpochDay(1), 100),
            createSeparator(LocalDate.ofEpochDay(2), 70),
        ) shouldBe false
    }

    "areItemsTheSame() returns false if the items are of different types" {
        diffCallback.areItemsTheSame(
            createSeparator(LocalDate.ofEpochDay(1), 70),
            createRecord(1),
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
    return Record(id, name, 0, UiMeasurementUnit.Millis, LocalDate.ofEpochDay(0), null)
}

fun createSeparator(date: LocalDate, totalCount: Long) : Separator {
    return Separator(
        date,
        Separator.Total(totalCount, UiMeasurementUnit.Millis),
    )
}
