package com.maxpoliakov.skillapp.shared.util

import java.time.Duration

inline fun <T> Iterable<T>.sumByDuration(selector: (T) -> Duration): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Array<Duration>.sum(): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum += element
    }
    return sum
}
