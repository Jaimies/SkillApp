package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.domain.model.Record
import java.time.Duration

interface RecordAddedSnackbarShower {
    fun showRecordAddedSnackbar(duration: Duration, recordToBeEdited: Record?)
}
