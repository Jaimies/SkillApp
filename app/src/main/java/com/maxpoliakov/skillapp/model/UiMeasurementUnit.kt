package com.maxpoliakov.skillapp.model

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.MappableEnum
import com.maxpoliakov.skillapp.shared.chart.valueformatter.DistanceFormatter
import com.maxpoliakov.skillapp.shared.chart.valueformatter.IntegerFormatter
import com.maxpoliakov.skillapp.shared.chart.valueformatter.TimeFormatter
import com.maxpoliakov.skillapp.shared.extensions.format
import com.maxpoliakov.skillapp.shared.picker.DistancePicker
import com.maxpoliakov.skillapp.shared.picker.DurationPicker
import com.maxpoliakov.skillapp.shared.picker.ValuePicker
import com.maxpoliakov.skillapp.shared.picker.ValuePicker.Value.GoalValue
import com.maxpoliakov.skillapp.shared.picker.ValuePicker.Value.RegularValue
import com.maxpoliakov.skillapp.shared.extensions.toReadableFloat
import com.maxpoliakov.skillapp.shared.picker.IntegerValuePicker
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

enum class UiMeasurementUnit : MappableEnum<UiMeasurementUnit, MeasurementUnit<*>> {
    Millis {
        override val totalCountStringResId = R.string.total_hours
        override val initialTimeResId = R.string.initial_time
        override val changeCountResId = R.string.change_time
        override val addRecordBtnResId = R.string.add_hours_record
        override val addRecordDialogTitleResId = R.string.add_time
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
        override fun createPicker() = DurationPicker()
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

        override fun toString(count: Long, context: Context): String {
            val kilometers = (count / 1000f).toReadableFloat()
            return context.getString(R.string.distance_kilometers, kilometers)
        }

        override fun getValueFormatter(context: Context) = DistanceFormatter(context)
        override fun createPicker() = DistancePicker()
        override fun toDomain() = MeasurementUnit.Meters
        override fun getInitialCount(countEnteredByUser: Long) = countEnteredByUser * 1000
    },

    Times {
        override val totalCountStringResId = R.string.total_times
        override val initialTimeResId = R.string.initial_count
        override val changeCountResId = R.string.change_count
        override val addRecordBtnResId = R.string.add_times_record
        override val nameResId = R.string.times

        override fun toString(count: Long, context: Context): String {
            return context.resources
                .getQuantityString(R.plurals.times_count, count.toInt(), count.toInt())
        }

        override fun toDomain() = MeasurementUnit.Times
    },

    Pages {
        override val totalCountStringResId = R.string.total_pages
        override val initialTimeResId = R.string.pages_already_read
        override val changeCountResId = R.string.change_number_of_pages
        override val nameResId = R.string.pages
        override val addRecordBtnResId = R.string.add_pages

        override fun toString(count: Long, context: Context): String {
            return context.resources
                .getQuantityString(R.plurals.pages, count.toInt(), count.toInt())
        }

        override fun toDomain() = MeasurementUnit.Pages
    };

    abstract val totalCountStringResId: Int
    abstract val initialTimeResId: Int
    abstract val changeCountResId: Int
    abstract val nameResId: Int
    abstract val addRecordBtnResId: Int
    open val addRecordDialogTitleResId get() = addRecordBtnResId
    open val isStopwatchEnabled get() = false

    abstract fun toString(count: Long, context: Context): String

    open fun getInitialCount(countEnteredByUser: Long): Long = countEnteredByUser
    open fun getValueFormatter(context: Context): ValueFormatter = IntegerFormatter()
    open fun createPicker(): ValuePicker<*> = IntegerValuePicker(this.toDomain())

    open fun toLongString(count: Long, context: Context): String {
        return toString(count, context)
    }

    open fun getRecordAddedString(count: Long, context: Context): String {
        return toString(count, context)
    }

    fun showPicker(
        fragmentManager: FragmentManager,
        initialCount: Long = 0,
        editMode: Boolean = false,
        onTimeSet: (count: Long) -> Unit
    ) {
        val picker = createPicker()

        picker.arguments = ValuePicker.Configuration(
            value = RegularValue(initialCount, this.toDomain()),
            isInEditMode = editMode,
        ).getArguments()

        picker.addOnPositiveButtonClickListener {
            val count = picker.count
            if (count > 0) onTimeSet(count)
        }

        picker.show(fragmentManager, null)
    }

    fun showGoalPicker(
        fragmentManager: FragmentManager,
        goal: Goal?,
        onGoalSet: (goal: Goal?) -> Unit
    ) {
        val dialog = createPicker()

        dialog.arguments = ValuePicker.Configuration(
            value = GoalValue(goal, this.toDomain()),
        ).getArguments()

        dialog.addOnPositiveButtonClickListener {
            onGoalSet(dialog.goal)
        }

        dialog.show(fragmentManager, null)
    }

    companion object : MappableEnum.Companion<UiMeasurementUnit, MeasurementUnit<*>>(values())
}
