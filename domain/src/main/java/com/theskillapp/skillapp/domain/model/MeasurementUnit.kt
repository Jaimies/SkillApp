package com.theskillapp.skillapp.domain.model

import java.time.Duration

sealed class MeasurementUnit<T> {
    abstract fun toType(value: Long): T
    abstract fun toLong(value: T): Long

    object Millis : MeasurementUnit<Duration>() {
        override fun toType(value: Long) = Duration.ofMillis(value)
        override fun toLong(value: Duration) = value.toMillis()
    }

    object Meters : MeasurementUnit<Distance>() {
        override fun toType(value: Long) = Distance.ofMeters(value)
        override fun toLong(value: Distance) = value.toMeters()
    }

    sealed class IntegerUnit : MeasurementUnit<Count>() {
        override fun toType(value: Long) = Count.ofTimes(value)
        override fun toLong(value: Count) = value.toTimes()
    }

    object Times : IntegerUnit()
    object Pages : IntegerUnit()
    object Steps: IntegerUnit()
    object Reps : IntegerUnit()
    object Kilograms: IntegerUnit()
    object Calories: IntegerUnit()
}
