package com.theskillapp.skillapp.domain.model

interface Trackable {
    val id: Int
    val totalCount: Long
    val unit: MeasurementUnit<*>
    val goal: Goal?
    val name: String
}
