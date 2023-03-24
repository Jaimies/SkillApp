package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import io.kotest.core.spec.style.StringSpec
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration

class RecordUtilImplTest : StringSpec({
    val snackbarShower = mockk<RecordAddedSnackbarShower>(relaxed = true)
    val recordUtil = RecordUtilImpl(snackbarShower)

    val record = Record(
        name = "",
        skillId = 1,
        count = Duration.ofHours(1).toMillis(),
        unit = MeasurementUnit.Millis,
    )

    val record2 = Record(
        name = "",
        skillId = 1,
        count = Duration.ofHours(2).toMillis(),
        unit = MeasurementUnit.Millis,
    )

    afterEach {
        clearMocks(snackbarShower)
    }

    "does not show anything if no records are added" {
        recordUtil.notifyRecordsAdded(listOf())
        verify { snackbarShower wasNot Called }
    }

    "shows a snackbar with the option to edit the record if one record is added" {
        recordUtil.notifyRecordsAdded(listOf(record))

        verify(exactly = 1) {
            snackbarShower.showRecordAddedSnackbar(Duration.ofHours(1), record)
        }
    }

    "shows a snackbar without the option to edit any records if more than one record is added" {
        recordUtil.notifyRecordsAdded(listOf(record, record2))

        verify(exactly = 1) {
            snackbarShower.showRecordAddedSnackbar(Duration.ofHours(3), null)
        }
    }
})
