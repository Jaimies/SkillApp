package com.jdevs.timeo.shared.collections

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <T> MutableList<T>.update(index: Int, transform: (T) -> T) {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    require(index >= 0) { "Index must greater than or equal to 0, got $index" }
    this[index] = transform(this[index])
}
