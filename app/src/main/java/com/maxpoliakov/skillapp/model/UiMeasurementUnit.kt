package com.maxpoliakov.skillapp.model

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.ui.common.picker.DistancePicker
import com.maxpoliakov.skillapp.ui.common.picker.DurationPicker
import com.maxpoliakov.skillapp.util.time.toReadableHours
import com.maxpoliakov.skillapp.util.ui.format
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.Duration

enum class UiMeasurementUnit {
    Millis {
        override val totalCountStringResId = R.string.total_hours
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

        override fun showPicker(
            context: Context,
            initialCount: Long,
            fragmentManager: FragmentManager,
            onTimeSet: (count: Long) -> Unit
        ) {
            val initialTime = Duration.ofMillis(initialCount)

            val dialog = DurationPicker.Builder()
                .setDuration(initialTime)
                .build()

            dialog.addOnPositiveButtonClickListener(View.OnClickListener {
                val time = dialog.duration
                if (time > Duration.ZERO)
                    onTimeSet(time.toMillis())
            })

            dialog.show(fragmentManager, null)
        }
    },

    Meters {
        override val totalCountStringResId = R.string.total_kilometers
        override val addRecordBtnResId = R.string.add_kilometers_record
        override val nameResId = R.string.kilometers
        override val isStopwatchEnabled = false

        override fun toShortString(count: Long, context: Context): String {
            return "${count / 1000.0} km"
        }

        override fun toLongString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getRecordAddedString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun showPicker(
            context: Context,
            initialCount: Long,
            fragmentManager: FragmentManager,
            onTimeSet: (count: Long) -> Unit
        ) {
            val dialog = DistancePicker.Builder()
                .setDistance(initialCount)
                .build()

            dialog.addOnPositiveButtonClickListener(View.OnClickListener {
                val count = dialog.distance
                if (count > 0) onTimeSet(count.toLong())
            })

            dialog.show(fragmentManager, null)
        }
    },

    Times {
        override val totalCountStringResId = R.string.total_times
        override val addRecordBtnResId = R.string.add_times_record
        override val nameResId = R.string.times
        override val isStopwatchEnabled = false

        override fun toShortString(count: Long, context: Context): String {
            return "$count times"
        }

        override fun toLongString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun getRecordAddedString(count: Long, context: Context): String {
            return toShortString(count, context)
        }

        override fun showPicker(
            context: Context,
            initialCount: Long,
            fragmentManager: FragmentManager,
            onTimeSet: (count: Long) -> Unit
        ) {
            // TODO actually show the picker
        }
    };

    abstract val totalCountStringResId: Int
    abstract val nameResId: Int
    abstract val addRecordBtnResId: Int
    abstract val isStopwatchEnabled: Boolean

    abstract fun toShortString(count: Long, context: Context): String
    abstract fun toLongString(count: Long, context: Context): String
    abstract fun getRecordAddedString(count: Long, context: Context): String
    abstract fun showPicker(
        context: Context,
        initialCount: Long = 0,
        fragmentManager: FragmentManager = context.getFragmentManager(),
        onTimeSet: (count: Long) -> Unit
    )

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
    }
}
