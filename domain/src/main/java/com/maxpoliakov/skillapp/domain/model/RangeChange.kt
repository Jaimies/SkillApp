package com.maxpoliakov.skillapp.domain.model

object RangeChange {
    data class Start<T : Comparable<T>>(val value: T) : Change<ClosedRange<T>> {
        override fun apply(range: ClosedRange<T>): ClosedRange<T> {
            return value..range.endInclusive
        }
    }

    data class End<T : Comparable<T>>(val value: T) : Change<ClosedRange<T>> {
        override fun apply(range: ClosedRange<T>): ClosedRange<T> {
            return range.start..value
        }
    }
}
