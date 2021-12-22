package com.maxpoliakov.skillapp.util.tracking

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

class RecordUtilImpl @Inject constructor(
        private val changeRecordTime: ChangeRecordTimeUseCase,
        private val ioScope: CoroutineScope
) : RecordUtil {

    override fun notifyRecordAdded(view: View, record: Record) {
        val snackbar = Snackbar.make(view, getLabel(view.context, record), Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.change_time) { editTime(view, record) }
        snackbar.setActionTextColor(view.context.getColorAttributeValue(R.attr.snackbarActionTextColor))
        snackbar.show()
    }

    private fun getLabel(context: Context, record: Record): String {
        if (record.time.toHours() == 0L) {
            if (record.time.toMinutes() == 0L) {
                return context.getString(R.string.record_added, record.time.seconds)
            }

            return context.getString(R.string.record_added_with_minutes, record.time.toMinutes())
        }

        return context.getString(
                R.string.record_added_with_hours,
                record.time.toHours(),
                record.time.toMinutesPartCompat()
        )
    }

    private fun editTime(view: View, record: Record) {
        view.context.showTimePicker(record.time) { newTime ->
            ioScope.launch {
                changeRecordTime.run(record.id, newTime)
            }
        }
    }
}
