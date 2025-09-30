package com.theskillapp.skillapp.model

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.Distance
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.model.UiGoal.Companion.mapToUI
import com.theskillapp.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.theskillapp.skillapp.shared.MappableEnum
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

    enum class Type(override val domainCounterpart: Goal.Type) : MappableEnum<Type, Goal.Type> {
        Daily(Goal.Type.Daily) {
            override val goalResId get() = R.string.goal_type_daily
            override val goalValueResId get() = R.string.daily_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(23).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(500).plusMeters(900))
                is MeasurementUnit.IntegerUnit -> 2_000
            }
        },
        Weekly(Goal.Type.Weekly) {
            override val goalResId get() = R.string.goal_type_weekly
            override val goalValueResId get() = R.string.weekly_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(167).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(2_000).plusMeters(900))
                is MeasurementUnit.IntegerUnit -> 10_000
            }
        },
        Monthly(Goal.Type.Monthly) {
            override val goalResId get() = R.string.goal_type_monthly
            override val goalValueResId get() = R.string.monthly_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(743).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(10_000).plusMeters(900))
                is MeasurementUnit.IntegerUnit -> 50_000
            }
        },
        Yearly(Goal.Type.Yearly) {
            override val goalResId get() = R.string.goal_type_yearly
            override val goalValueResId get() = R.string.yearly_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(8783).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(30_000).plusMeters(900))
                is MeasurementUnit.IntegerUnit -> 500_000
            }
        },
        Lifetime(Goal.Type.Lifetime) {
            override val goalResId get() = R.string.goal_type_lifetime
            override val goalValueResId get() = R.string.lifetime_goal_value

            override fun <T> getMaximumCount(unit: MeasurementUnit<T>) = when (unit) {
                is MeasurementUnit.Millis -> unit.toLong(Duration.ofHours(50_000).plusMinutes(59))
                is MeasurementUnit.Meters -> unit.toLong(Distance.ofKilometers(80_000).plusMeters(900))
                is MeasurementUnit.IntegerUnit -> 500_000
            }
        };

        abstract val goalResId: Int
        abstract val goalValueResId: Int

        abstract fun <T> getMaximumCount(unit: MeasurementUnit<T>): Long

        val interval = this.domainCounterpart.interval

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

fun UiGoal.mapToDomain() = Goal(count, type.domainCounterpart)

@JvmName("mapToUI_withUIMeasurementUnit")
fun Flow<Goal?>.mapToUI(unit: Flow<UiMeasurementUnit>): Flow<UiGoal?> {
    return mapToUI(unit.map { it.domainCounterpart })
}

fun Flow<Goal?>.mapToUI(unit: Flow<MeasurementUnit<*>>): Flow<UiGoal?> {
    return combine(unit) { goal, unit -> goal?.mapToUI(unit) }
}
