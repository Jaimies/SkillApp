package com.maxpoliakov.skillapp.model

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.ui.common.picker.DistanceGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.DistancePicker
import com.maxpoliakov.skillapp.ui.common.picker.DurationGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.DurationPicker
import com.maxpoliakov.skillapp.ui.common.picker.GoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.TimesGoalPicker
import com.maxpoliakov.skillapp.ui.common.picker.TimesPicker
import com.maxpoliakov.skillapp.ui.common.picker.ValuePicker
import com.maxpoliakov.skillapp.util.time.toReadableFloat
import com.maxpoliakov.skillapp.util.time.toReadableHours
import com.maxpoliakov.skillapp.util.ui.format
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.Duration

enum class UiMeasurementUnit {
    Millis {
        override val totalCountStringResId = R.string.total_hours
        override val initialTimeResId = R.string.initial_time
        override val changeCountResId = R.string.change_time
        override val addRecordBtnResId = R.string.add_hours_record
        override val nameResId = R.string.hours
        override val isStopwatchEnabled = true

        override fun toShortString(count: Long, context: Context): String {
            val duration = Duration.ofMillis(count)
            val readableHours = duration.toReadableHours()
            return context.getString(R.string.time_hours, readableHours)
        }

        override fun toLongString(count: Long, context: Context): String {
            val time = Duration.ofMillis(count)
            return time.format(context)
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

        override fun getValueFormatter(context: Context) = Formatter(context)
        override fun toDomain() = MeasurementUnit.Millis
        override fun getValuePickerBuilder() = DurationPicker.Builder()
        override fun getGoalPickerBuilder() = DurationGoalPicker.Builder()
        override fun getInitialCount(countEnteredByUser: Long): Long {
            return Duration.ofHours(countEnteredByUser).toMillis()
        }

        inner class Formatter(private val context: Context) : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val duration = Duration.ofMillis(value.toLong())

                if (value == 0f) return ""
                if (duration < Duration.ofMinutes(1)) return context.getString(R.string.time_minutes, "1")
                if (duration >= Duration.ofHours(1)) return toHours(duration)
                return toMinutes(duration)
            }

            private fun toMinutes(duration: Duration): String {
                return context.getString(R.string.time_minutes, duration.toMinutes().toString())
            }

            private fun toHours(duration: Duration): String {
                return context.getString(R.string.time_hours, (duration.toMinutes() / 60f).toReadableFloat())
            }
        }
    },

    Meters {
        override val totalCountStringResId = R.string.total_kilometers
        override val initialTimeResId = R.string.initial_distance
        override val changeCountResId = R.string.change_distance
        override val addRecordBtnResId = R.string.add_kilometers_record
        override val nameResId = R.string.kilometers
        override val isStopwatchEnabled = false

        override fun toShortString(count: Long, context: Context): String {
            val kilometers = (count / 1000f).toReadableFloat()
            return context.getString(R.string.distance_kilometers, kilometers)
        }

        override fun toLongString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getRecordAddedString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getValueFormatter(context: Context) = Formatter(context)
        override fun getValuePickerBuilder() = DistancePicker.Builder()
        override fun getGoalPickerBuilder() = DistanceGoalPicker.Builder()
        override fun toDomain() = MeasurementUnit.Meters
        override fun getInitialCount(countEnteredByUser: Long) = countEnteredByUser * 1000

        inner class Formatter(private val context: Context) : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value == 0f) return ""
                return toShortString(value.toLong(), context)
            }
        }
    },

    Times {
        override val totalCountStringResId = R.string.total_times
        override val initialTimeResId = R.string.initial_count
        override val changeCountResId = R.string.change_count
        override val addRecordBtnResId = R.string.add_times_record
        override val nameResId = R.string.times
        override val isStopwatchEnabled = false

        override fun toShortString(count: Long, context: Context): String {
            return context.resources
                .getQuantityString(R.plurals.times_count, count.toInt())
                .format(count)
        }

        override fun toLongString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getRecordAddedString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getValueFormatter(context: Context) = Formatter()
        override fun getValuePickerBuilder() = TimesPicker.Builder()
        override fun getGoalPickerBuilder() = TimesGoalPicker.Builder()
        override fun toDomain() = MeasurementUnit.Times
        override fun getInitialCount(countEnteredByUser: Long) = countEnteredByUser

        inner class Formatter : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value == 0f) return ""
                return value.toInt().toString()
            }
        }
    };

    abstract val totalCountStringResId: Int
    abstract val initialTimeResId: Int
    abstract val changeCountResId: Int
    abstract val nameResId: Int
    abstract val addRecordBtnResId: Int
    abstract val isStopwatchEnabled: Boolean

    abstract fun toShortString(count: Long, context: Context): String
    abstract fun toLongString(count: Long, context: Context): String
    abstract fun getRecordAddedString(count: Long, context: Context): String
    abstract fun getValueFormatter(context: Context): ValueFormatter
    abstract fun getInitialCount(countEnteredByUser: Long): Long

    abstract fun getValuePickerBuilder(): ValuePicker.Builder
    abstract fun getGoalPickerBuilder(): GoalPicker.Builder

    abstract fun toDomain(): MeasurementUnit

    fun showPicker(
        context: Context,
        initialCount: Long = 0,
        fragmentManager: FragmentManager = context.getFragmentManager(),
        onTimeSet: (count: Long) -> Unit
    ) {
        val picker = getValuePickerBuilder()
            .setCount(initialCount)
            .build()

        picker.addOnPositiveButtonClickListener(View.OnClickListener {
            val timesCount = picker.count
            if (timesCount > 0) onTimeSet(timesCount)
        })

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
            .build() as GoalPicker<*>

        dialog.addOnPositiveButtonClickListener(View.OnClickListener {
            onGoalSet(dialog.goal)
        })

        dialog.show(fragmentManager, null)
    }

    fun formatGoal(context: Context, goal: Goal?): String {
        if (goal == null) return context.getString(R.string.goal_not_set)

        val resId = if (goal.type == Goal.Type.Daily) R.string.daily_goal_without_progress
        else R.string.weekly_goal_without_progress

        return context.getString(resId, toShortString(goal.count, context))
    }

    fun formatGoal(context: Context, goal: Goal?, completedCount: Long?): String {
        if (goal == null || completedCount == null) return ""

        val stringResId = if (goal.type == Goal.Type.Daily) R.string.daily_goal else R.string.weekly_goal

        return context.getString(
            stringResId,
            toShortString(completedCount, context),
            toShortString(goal.count, context)
        )
    }

    companion object {
        fun from(unit: MeasurementUnit): UiMeasurementUnit {
            return when (unit) {
                MeasurementUnit.Millis -> Millis
                MeasurementUnit.Meters -> Meters
                MeasurementUnit.Times -> Times
            }
        }

        fun MeasurementUnit.mapToUI() = from(this)
    }
}
