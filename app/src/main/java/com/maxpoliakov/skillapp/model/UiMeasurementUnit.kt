package com.maxpoliakov.skillapp.model

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.MappableEnum
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.ui.chart.valueformatter.CountFormatter
import com.maxpoliakov.skillapp.ui.chart.valueformatter.DistanceFormatter
import com.maxpoliakov.skillapp.ui.chart.valueformatter.TimeFormatter
import com.maxpoliakov.skillapp.ui.common.picker.DistanceGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.DistancePicker
import com.maxpoliakov.skillapp.ui.common.picker.DurationGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.DurationPicker
import com.maxpoliakov.skillapp.ui.common.picker.GoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.TimesGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.TimesPicker
import com.maxpoliakov.skillapp.ui.common.picker.ValuePicker
import com.maxpoliakov.skillapp.util.time.toReadableFloat
import com.maxpoliakov.skillapp.util.ui.format
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.Duration

enum class UiMeasurementUnit : MappableEnum<UiMeasurementUnit, MeasurementUnit<*>> {
    Millis {
        override val totalCountStringResId = R.string.total_hours
        override val initialTimeResId = R.string.initial_time
        override val changeCountResId = R.string.change_time
        override val addRecordBtnResId = R.string.add_hours_record
        override val nameResId = R.string.hours
        override val isStopwatchEnabled = true

        override fun toString(count: Long, context: Context): String {
            return Duration.ofMillis(count).format(context, R.string.time_hours_and_minutes_nbsp)
        }

        override fun toLongString(count: Long, context: Context): String {
            return Duration.ofMillis(count).format(context)
        }

        override fun getRecordAddedString(count: Long, context: Context): String {
            val time = Duration.ofMillis(count)

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

        override fun getValueFormatter(context: Context) = TimeFormatter(context)
        override fun toDomain() = MeasurementUnit.Millis
        override fun getValuePickerBuilder() = DurationPicker.Builder()
        override fun getGoalPickerBuilder() = DurationGoalPicker.Builder()
        override fun getInitialCount(countEnteredByUser: Long): Long {
            return Duration.ofHours(countEnteredByUser).toMillis()
        }
    },

    Meters {
        override val totalCountStringResId = R.string.total_kilometers
        override val initialTimeResId = R.string.initial_distance
        override val changeCountResId = R.string.change_distance
        override val addRecordBtnResId = R.string.add_kilometers_record
        override val nameResId = R.string.kilometers
        override val isStopwatchEnabled = false

        override fun toString(count: Long, context: Context): String {
            val kilometers = (count / 1000f).toReadableFloat()
            return context.getString(R.string.distance_kilometers, kilometers)
        }

        override fun getValueFormatter(context: Context) = DistanceFormatter(context)
        override fun getValuePickerBuilder() = DistancePicker.Builder()
        override fun getGoalPickerBuilder() = DistanceGoalPicker.Builder()
        override fun toDomain() = MeasurementUnit.Meters
        override fun getInitialCount(countEnteredByUser: Long) = countEnteredByUser * 1000
    },

    Times {
        override val totalCountStringResId = R.string.total_times
        override val initialTimeResId = R.string.initial_count
        override val changeCountResId = R.string.change_count
        override val addRecordBtnResId = R.string.add_times_record
        override val nameResId = R.string.times
        override val isStopwatchEnabled = false

        override fun toString(count: Long, context: Context): String {
            return context.resources
                .getQuantityString(R.plurals.times_count, count.toInt())
                .format(count)
        }

        override fun getValueFormatter(context: Context) = CountFormatter()
        override fun getValuePickerBuilder() = TimesPicker.Builder()
        override fun getGoalPickerBuilder() = TimesGoalPicker.Builder()
        override fun toDomain() = MeasurementUnit.Times
        override fun getInitialCount(countEnteredByUser: Long) = countEnteredByUser
    };

    abstract val totalCountStringResId: Int
    abstract val initialTimeResId: Int
    abstract val changeCountResId: Int
    abstract val nameResId: Int
    abstract val addRecordBtnResId: Int
    abstract val isStopwatchEnabled: Boolean

    abstract fun toString(count: Long, context: Context): String

    abstract fun getValueFormatter(context: Context): ValueFormatter
    abstract fun getInitialCount(countEnteredByUser: Long): Long

    abstract fun getValuePickerBuilder(): ValuePicker.Builder
    abstract fun getGoalPickerBuilder(): GoalPicker.Builder

    open fun toLongString(count: Long, context: Context): String {
        return toString(count, context)
    }

    open fun getRecordAddedString(count: Long, context: Context): String {
        return toString(count, context)
    }

    fun showPicker(
        context: Context,
        initialCount: Long = 0,
        editMode: Boolean = false,
        fragmentManager: FragmentManager = context.getFragmentManager(),
        onTimeSet: (count: Long) -> Unit
    ) {
        val picker = getValuePickerBuilder()
            .setCount(initialCount)
            .setEditModeEnabled(editMode)
            .build()

        picker.addOnConfirmedListener { count ->
            if (count > 0) onTimeSet(count)
        }

        picker.show(fragmentManager, null)
    }

    fun showGoalPicker(
        context: Context,
        goal: Goal?,
        fragmentManager: FragmentManager = context.getFragmentManager(),
        onGoalSet: (goal: Goal?) -> Unit
    ) {
        val dialog = getGoalPickerBuilder()
            .setGoal(goal)
            .build()

        dialog.addOnConfirmedListener(onGoalSet)
        dialog.show(fragmentManager, null)
    }

    companion object : MappableEnum.Companion<UiMeasurementUnit, MeasurementUnit<*>>(values())
}
