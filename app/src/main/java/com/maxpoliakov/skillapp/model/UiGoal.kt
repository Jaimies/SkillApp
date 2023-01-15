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

            override fun toDomain() = Goal.Type.Daily
        },
        Weekly {
            override val goalResId get() = R.string.plan_weekly
            override val goalWithValueAndProgressResId get() = R.string.weekly_goal
            override val goalWithValueResId get() = R.string.weekly_goal_without_progress

            override fun toDomain() = Goal.Type.Weekly
        };

        abstract val goalResId: Int
        abstract val goalWithValueAndProgressResId: Int
        abstract val goalWithValueResId: Int

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
