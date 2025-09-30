package com.theskillapp.skillapp.domain.model

interface RangeChange<T : Comparable<T>> : Change<ClosedRange<T>> {
    data class Start<T : Comparable<T>>(val value: T) : RangeChange<T> {
        override fun apply(range: ClosedRange<T>): ClosedRange<T> {
            return value..range.endInclusive
        }
    }

    data class End<T : Comparable<T>>(val value: T) : RangeChange<T> {
        override fun apply(range: ClosedRange<T>): ClosedRange<T> {
            return range.start..value
        }
    }
}
