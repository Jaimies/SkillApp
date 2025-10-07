package com.theskillapp.skillapp.model

import com.theskillapp.skillapp.domain.model.Trackable
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI

data class ProductivitySummary(
    val totalCount: Long,
    val lastWeekCount: Long,
    val unit: UiMeasurementUnit,
) {
    companion object {
        val ZERO = ProductivitySummary(0L, 0L, UiMeasurementUnit.Millis)

        fun from(trackable: Trackable, lastWeekCount: Long): ProductivitySummary {
            return ProductivitySummary(
                totalCount = trackable.totalCount,
                lastWeekCount = lastWeekCount,
                unit = trackable.unit.mapToUI(),
            )
        }
    }
}
