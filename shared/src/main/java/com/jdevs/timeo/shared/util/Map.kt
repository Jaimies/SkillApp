package com.jdevs.timeo.shared.util

fun <K, V> Map<K, V>.sumBy(predicate: (Map.Entry<K, V>) -> Int): Int {
    var total = 0

    this.forEach { entry ->
        total += predicate(entry)
    }

    return total
}