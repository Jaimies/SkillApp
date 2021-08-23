package com.maxpoliakov.skillapp.shared.util

import java.time.Duration

fun <T> Iterable<T>.sumByDuration(selector: (T) -> Duration): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
