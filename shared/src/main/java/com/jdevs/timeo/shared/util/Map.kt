package com.jdevs.timeo.shared.util
import java.time.Duration

fun Map<*, Duration>.sumByDuration(): Duration {
    var total = Duration.ZERO

    this.forEach { entry ->
        total += entry.value
    }

    return total
}
