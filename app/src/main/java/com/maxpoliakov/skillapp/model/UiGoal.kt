package com.maxpoliakov.skillapp.model

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class UiGoal(
    val count: Long,
    val type: Type,
    val unit: UiMeasurementUnit,
) {
    fun format(context: Context): String {
        return context.getString(
            type.goalWithoutProgressStringResId,
            unit.toShortString(count, context)
        )
    }

    fun format(context: Context, completedCount: Long?): String {
        if (completedCount == null) return ""

        return context.getString(
            type.goalStringResId,
            unit.toShortString(completedCount, context),
            unit.toShortString(count, context),
        )
    }

    enum class Type : MappableEnum<Type, Goal.Type> {
        Daily {
            override val goalNameStringResId get() = R.string.plan_daily
            override val goalStringResId get() = R.string.daily_goal
            override val goalWithoutProgressStringResId get() = R.string.daily_goal_without_progress

            override fun toDomain() = Goal.Type.Daily
        },
        Weekly {
            override val goalNameStringResId get() = R.string.plan_weekly
            override val goalStringResId get() = R.string.weekly_goal
            override val goalWithoutProgressStringResId get() = R.string.weekly_goal_without_progress

            override fun toDomain() = Goal.Type.Weekly
        };

        abstract val goalNameStringResId: Int
        abstract val goalStringResId: Int
        abstract val goalWithoutProgressStringResId: Int

        companion object : MappableEnum.Companion<Type, Goal.Type>(values())
    }

    companion object {
        fun from(goal: Goal, unit: MeasurementUnit): UiGoal {
            return UiGoal(
                goal.count,
                goal.type.mapToUI(),
                UiMeasurementUnit.from(unit),
            )
        }

        fun Goal.mapToUI(unit: MeasurementUnit) = from(this, unit)
    }
}

fun UiGoal.mapToDomain() = Goal(count, type.toDomain())

fun Flow<Goal?>.mapToUI(unit: Flow<UiMeasurementUnit>): Flow<UiGoal?> {
    return combine(unit) { goal, unit -> goal?.mapToUI(unit.toDomain()) }
}
