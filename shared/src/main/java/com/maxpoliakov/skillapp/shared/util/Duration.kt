package com.maxpoliakov.skillapp.shared.util

import java.time.Duration

fun <T> Iterable<T>.sumByDuration(selector: (T) -> Duration): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun <T> Iterable<T>.averageByDuration(selector: (T) -> Duration): Duration {
    var total = Duration.ZERO
    var count = 0L
    for (element in this) {
        total += selector(element)
        count++
    }
    return if (count == 0L) Duration.ZERO else total.dividedBy(count)
}
