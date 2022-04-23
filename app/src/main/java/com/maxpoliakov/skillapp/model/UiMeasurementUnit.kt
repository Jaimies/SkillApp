package com.maxpoliakov.skillapp.model

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.time.toReadableHours
import com.maxpoliakov.skillapp.util.ui.format
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
    };

    abstract val totalCountStringResId: Int
    abstract val nameResId: Int
    abstract val addRecordBtnResId: Int
    abstract val isStopwatchEnabled: Boolean

    abstract fun toShortString(count: Long, context: Context): String
    abstract fun toLongString(count: Long, context: Context): String
    abstract fun getRecordAddedString(count: Long, context: Context): String

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
