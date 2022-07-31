package com.maxpoliakov.skillapp.domain.model

interface Trackable {
    val id: Int
    val totalCount: Long
    val unit: MeasurementUnit
    val goal: Goal?
}
