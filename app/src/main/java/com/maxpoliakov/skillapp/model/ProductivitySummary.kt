package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

data class ProductivitySummary(
    val totalCount: Long,
    val lastWeekCount: Long,
    val unit: UiMeasurementUnit,
) {
    companion object {
        val ZERO = ProductivitySummary(0L, 0L, UiMeasurementUnit.Millis)

        fun from(skill: Skill) : ProductivitySummary {
            return ProductivitySummary(skill.totalCount, skill.lastWeekCount, skill.unit.mapToUI())
        }
    }
}
