package com.maxpoliakov.skillapp.shared.tracking

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.Duration
import javax.inject.Inject

class SnackbarLabelFormatterImpl @Inject constructor(
    @ActivityContext
    private val context: Context,
): SnackbarLabelFormatter {
    private fun getLabel(time: Duration): String {
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

    override fun getLabel(records: List<Record>): String {
        return getLabel(records.getTotalDuration())
    }

    private fun List<Record>.getTotalDuration(): Duration {
        return sumByDuration { record -> Duration.ofMillis(record.count) }
    }
}
