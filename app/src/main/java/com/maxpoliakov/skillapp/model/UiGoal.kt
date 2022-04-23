package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Goal

data class UiGoal(
    val goal: Goal,
    val unit: UiMeasurementUnit,
)
