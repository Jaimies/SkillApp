package com.theskillapp.skillapp.shared.tracking

import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.shared.util.sumByDuration
import dagger.hilt.android.scopes.ActivityScoped
import java.time.Duration
import javax.inject.Inject

@ActivityScoped
class RecordUtilImpl @Inject constructor(
    private val recordAddedSnackbarShower: RecordAddedSnackbarShower,
) : RecordUtil {

    override fun notifyRecordsAdded(records: List<Record>) {
        if (records.isEmpty()) return

        recordAddedSnackbarShower.showRecordAddedSnackbar(
            duration = records.sumByDuration { Duration.ofMillis(it.count) },
            recordToBeEdited = getRecordToBeEdited(records),
        )
    }

    private fun getRecordToBeEdited(records: List<Record>): Record? {
        if (records.size == 1) return records[0]
        return null
    }
}
