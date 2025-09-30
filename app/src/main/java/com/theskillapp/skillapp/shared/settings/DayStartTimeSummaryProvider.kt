package com.theskillapp.skillapp.shared.settings

import androidx.preference.Preference
import com.theskillapp.skillapp.R
import java.time.LocalTime

class DayStartTimeSummaryProvider: Preference.SummaryProvider<TimePickerPreference> {
    override fun provideSummary(preference: TimePickerPreference): CharSequence {
        if (preference.value == LocalTime.MIDNIGHT) {
            return preference.context.getString(R.string.day_start_time_default_summary)
        }

        return preference.context.getString(R.string.day_start_time_summary, preference.formattedValue)
    }
}
