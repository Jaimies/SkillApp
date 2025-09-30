package com.theskillapp.skillapp.shared.tracking

import com.theskillapp.skillapp.domain.model.Record
import java.time.Duration

interface RecordAddedSnackbarShower {
    fun showRecordAddedSnackbar(duration: Duration, recordToBeEdited: Record?)
}
