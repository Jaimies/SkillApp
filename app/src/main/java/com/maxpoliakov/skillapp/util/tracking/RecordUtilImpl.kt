package com.maxpoliakov.skillapp.util.tracking

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

class RecordUtilImpl @Inject constructor(
    private val changeRecordTime: ChangeRecordTimeUseCase,
    private val ioScope: CoroutineScope
) : RecordUtil {

    override fun notifyRecordAdded(view: View, record: Record, unit: UiMeasurementUnit) {
        val snackbar = Snackbar.make(view, getLabel(view.context, record), Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.change_time) { editTime(view, record, unit) }
        snackbar.setActionTextColor(view.context.getColorAttributeValue(R.attr.snackbarActionTextColor))
        snackbar.show()
    }

    private fun getLabel(context: Context, record: Record): String {
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

    private fun editTime(view: View, record: Record, unit: UiMeasurementUnit) {
        unit.showPicker(view.context, record.count) { newTime ->
            ioScope.launch {
                changeRecordTime.run(record.id, newTime)
            }
        }
    }
}
