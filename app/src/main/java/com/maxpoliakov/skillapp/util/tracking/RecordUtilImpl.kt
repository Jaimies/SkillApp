package com.maxpoliakov.skillapp.util.tracking

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.di.SnackbarRoot
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@ActivityScoped
class RecordUtilImpl @Inject constructor(
    private val editRecord: EditRecordUseCase,
    private val ioScope: CoroutineScope,
    @SnackbarRoot
    private val snackbarRoot: View,
) : RecordUtil {

    private val context: Context get() = snackbarRoot.context

    override fun notifyRecordAdded(record: Record) {
        Snackbar.make(snackbarRoot, getLabel(record), Snackbar.LENGTH_LONG)
            .setAction(R.string.change_time) { editTime(record) }
            .setActionTextColor(context.getColorAttributeValue(R.attr.snackbarActionTextColor))
            .show()
    }

    private fun getLabel(record: Record): String {
        val time = Duration.ofMillis(record.count)

        if (time.toHours() == 0L) {
            if (time.toMinutes() == 0L) {
                return context.getString(R.string.record_added, time.seconds)
            }

            return context.getString(R.string.record_added_with_minutes, time.toMinutes())
        }

        return context.getString(
            R.string.record_added_with_hours,
            time.toHours(),
            time.toMinutesPartCompat()
        )
    }

    private fun editTime(record: Record) {
        UiMeasurementUnit.Millis.showPicker(context, record.count, editMode = true) { newTime ->
            ioScope.launch {
                editRecord.changeCount(record.id, newTime)
            }
        }
    }
}
