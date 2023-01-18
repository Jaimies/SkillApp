package com.maxpoliakov.skillapp.ui.common.picker

import android.content.SharedPreferences
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.persistence.getStringPreference
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class DurationPicker : ValuePicker<Duration>(MeasurementUnit.Millis) {
    override val value: Duration
        get() = Duration.ZERO
            .plusHours(firstPicker.value.toLong())
            .plusMinutes(secondPicker.value.toLong() * minutePickerInterval)

    private val minutePickerInterval by lazy {
        if (getPickerIntervalPreference() == "1_min") 1 else 5
    }

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun getFirstPickerValues() = Array(24) { index ->
        requireContext().getString(R.string.time_hours, index.toString())
    }

    override fun getSecondPickerValues() = Array(60 / minutePickerInterval) { index ->
        requireContext().getString(R.string.time_minutes, (index * minutePickerInterval).toString())
    }

    private fun getPickerIntervalPreference(): String {
        return sharedPrefs.getStringPreference("duration_picker_interval", "5_min")
    }

    class Builder : ValuePicker.Builder<Duration>(MeasurementUnit.Millis) {
        override var titleTextResId = R.string.add_time
        override val titleTextInEditModeResId = R.string.change_time
        override val maxValue get() = Duration.ofHours(23).plusMinutes(55)

        override fun createDialog() = DurationPicker()

        override fun setValue(value: Duration) {
            setFirstPickerValue(value.toHours().toInt())
            setSecondPickerValue(value.toMinutesPartCompat().toInt() / 5)
        }
    }
}
