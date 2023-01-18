package com.maxpoliakov.skillapp.domain.model

import java.time.Duration

sealed class MeasurementUnit<T> {
    abstract fun toType(value: Long): T
    abstract fun toLong(value: T): Long

    object Millis : MeasurementUnit<Duration>() {
        override fun toType(value: Long) = Duration.ofMillis(value)
        override fun toLong(value: Duration) = value.toMillis()
    }

    object Meters : MeasurementUnit<Long>() {
        override fun toType(value: Long) = value
        override fun toLong(value: Long) = value
    }

    object Times : MeasurementUnit<Long>() {
        override fun toType(value: Long) = value
        override fun toLong(value: Long) = value
    }
}
