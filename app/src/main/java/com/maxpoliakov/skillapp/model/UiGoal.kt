package com.maxpoliakov.skillapp.model

import android.content.Context
import com.maxpoliakov.skillapp.R
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
            type.goalValueResId,
            unit.toString(count, context),
        )
    }

    fun format(context: Context, completedCount: Long?): String {
        if (completedCount == null) return ""

        return context.getString(
            type.goalValueResId,
            context.getString(
                R.string.goal_progress,
                unit.toString(completedCount, context),
                unit.toString(count, context),
            )
        )
    }

    enum class Type : MappableEnum<Type, Goal.Type> {
        Daily {
            override val goalResId get() = R.string.goal_type_daily
            override val goalValueResId get() = R.string.daily_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(23).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(500).plusMeters(900))
                is MeasurementUnit.Times -> 2_000
                is MeasurementUnit.Pages -> 2_000
            }

            override fun toDomain() = Goal.Type.Daily
        },
        Weekly {
            override val goalResId get() = R.string.goal_type_weekly
            override val goalValueResId get() = R.string.weekly_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(167).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(2_000).plusMeters(900))
                is MeasurementUnit.Times -> 10_000
                is MeasurementUnit.Pages -> 10_000
            }

            override fun toDomain() = Goal.Type.Weekly
        },
        Lifetime {
            override val goalResId get() = R.string.goal_type_lifetime
            override val goalValueResId get() = R.string.lifetime_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(50_000).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(80_000).plusMeters(900))
                is MeasurementUnit.Times -> 500_000
                is MeasurementUnit.Pages -> 500_000
            }

            override fun toDomain() = Goal.Type.Lifetime
        };

        abstract val goalResId: Int
        abstract val goalValueResId: Int

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
