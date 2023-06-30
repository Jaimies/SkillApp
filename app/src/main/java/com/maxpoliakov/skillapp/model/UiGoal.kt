package com.maxpoliakov.skillapp.model

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.MappableEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.Duration

data class UiGoal(
    val count: Long,
    val type: Type,
    val unit: UiMeasurementUnit,
) {
    fun format(context: Context): String {
        return context.getString(
            type.goalWithValueResId,
            unit.toString(count, context)
        )
    }

    fun format(context: Context, completedCount: Long?): String {
        if (completedCount == null) return ""

        return context.getString(
            type.goalWithValueAndProgressResId,
            unit.toString(completedCount, context),
            unit.toString(count, context),
        )
    }

    enum class Type : MappableEnum<Type, Goal.Type> {
        Daily {
            override val goalResId get() = R.string.plan_daily
            override val goalWithValueAndProgressResId get() = R.string.daily_goal
            override val goalWithValueResId get() = R.string.daily_goal_without_progress

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(23).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(1999).plusMeters(900))
                is MeasurementUnit.Times -> 1999
                is MeasurementUnit.Pages -> 1999
            }

            override fun toDomain() = Goal.Type.Daily
        },
        Weekly {
            override val goalResId get() = R.string.plan_weekly
            override val goalWithValueAndProgressResId get() = R.string.weekly_goal
            override val goalWithValueResId get() = R.string.weekly_goal_without_progress

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(167).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(9999).plusMeters(900))
                is MeasurementUnit.Times -> 9999
                is MeasurementUnit.Pages -> 9999
            }

            override fun toDomain() = Goal.Type.Weekly
        },

        Lifetime {
            override val goalResId get() = R.string.plan_lifetime
            override val goalWithValueAndProgressResId get() = R.string.lifetime_goal
            override val goalWithValueResId get() = R.string.lifetime_goal_without_progress

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(1_000_000).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(1_000_000).plusMeters(900))
                is MeasurementUnit.Times -> 1_000_000
                is MeasurementUnit.Pages -> 1_000_000
            }

            override fun toDomain() = Goal.Type.Lifetime
        };

        abstract val goalResId: Int
        abstract val goalWithValueAndProgressResId: Int
        abstract val goalWithValueResId: Int

        abstract fun <T> getMaximumCount(unit: MeasurementUnit<T>): Long

        companion object : MappableEnum.Companion<Type, Goal.Type>(values())
    }

    companion object {
        fun from(goal: Goal, unit: MeasurementUnit<*>): UiGoal {
            return UiGoal(
                goal.count,
                goal.type.mapToUI(),
                unit.mapToUI(),
            )
        }

        fun Goal.mapToUI(unit: MeasurementUnit<*>) = from(this, unit)
    }
}

fun UiGoal.mapToDomain() = Goal(count, type.toDomain())

@JvmName("mapToUI_withUIMeasurementUnit")
fun Flow<Goal?>.mapToUI(unit: Flow<UiMeasurementUnit>): Flow<UiGoal?> {
    return mapToUI(unit.map(UiMeasurementUnit::toDomain))
}

fun Flow<Goal?>.mapToUI(unit: Flow<MeasurementUnit<*>>): Flow<UiGoal?> {
    return combine(unit) { goal, unit -> goal?.mapToUI(unit) }
}
